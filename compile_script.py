import sys
import xml.etree.ElementTree as ET
import subprocess
import time
import glob
import os
import shutil

def modify_log4j_config():
    """修改 src/main/resources/log4j2.xml 里的 status 为 OFF"""
    try:
        tree = ET.parse('src/main/resources/log4j2.xml')
        root = tree.getroot()
        if 'status' in root.attrib and root.attrib['status'] == 'WARN':
            root.attrib['status'] = 'OFF'
            tree.write('src/main/resources/log4j2.xml', encoding='utf-8', xml_declaration=True)
            print("log4j2.xml 配置已更新")
        else:
            print("log4j2.xml 无需更新")
    except FileNotFoundError:
        print("未找到 log4j2.xml 文件，跳过配置更新")
    except Exception as e:
        print(f"更新 log4j2.xml 配置时出错: {e}")

def run_tests():
    """运行测试并返回测试是否成功"""
    print("正在执行测试...")
    try:
        test_command = subprocess.run(["mvn", "test"], capture_output=True, text=True, check=True)
        print("测试通过")
        return True
    except subprocess.CalledProcessError as e:
        print("测试失败，终止编译")
        print("错误信息:")
        print(e.stderr)
        return False

def compile_project():
    """编译项目"""
    print("正在编译主项目...")
    try:
        subprocess.run(["mvn", "clean", "package"], check=True)
        print("主项目编译成功")
    except subprocess.CalledProcessError as e:
        print(f"主项目编译失败: {e}")
        sys.exit(1)

    print("正在编译 ipc 目录下的项目...")
    try:
        ipc_src_dir = "src/main/java/org/cookiebyte/dev/ipc"
        output_dir = "target/classes/org/cookiebyte/dev/ipc"
        jar_output_dir = "target/jars"
        jar_name = "CookieByte_IPC.jar"
        dependency_dir = "target/dependency-jars"

        # 创建输出目录
        subprocess.run(["mkdir", "-p", output_dir], check=True)
        subprocess.run(["mkdir", "-p", jar_output_dir], check=True)

        # 查找 Java 文件
        java_files = glob.glob(f"{ipc_src_dir}/*.java")
        if not java_files:
            print("未找到需要编译的 Java 文件，跳过 ipc 项目编译")
            return
        print("找到的 Java 文件:")
        for file in java_files:
            print(file)

        # 构建依赖的类路径
        dependency_jars = glob.glob(f"{dependency_dir}/*.jar")
        classpath = ":".join(dependency_jars)
        if classpath:
            classpath = f"target/classes:{classpath}"
        else:
            classpath = "target/classes"

        # 编译 Java 文件，添加 -classpath 参数
        javac_command = ["javac", "-d", "target/classes", "-classpath", classpath] + java_files
        subprocess.run(javac_command, check=True)
        print("ipc Java 文件编译成功")

        # 处理清单文件
        manifest_src = "src/main/resources/META-INF/MANIFEST.MF"
        manifest_dest = "target/classes/META-INF"
        subprocess.run(["mkdir", "-p", manifest_dest], check=True)
        if os.path.exists(manifest_src):
            subprocess.run(["cp", manifest_src, manifest_dest], check=True)
            print("清单文件复制成功")
        else:
            # 创建简单的 MANIFEST.MF 文件
            manifest_content = "Manifest-Version: 1.0\n"
            manifest_file = os.path.join(manifest_dest, "MANIFEST.MF")
            with open(manifest_file, "w") as f:
                f.write(manifest_content)
            print("新的清单文件已创建")

        # 打包 JAR 文件
        jar_command = ["jar", "cvfm", os.path.join(jar_output_dir, jar_name), os.path.join(manifest_dest, "MANIFEST.MF"), "-C", "target/classes", "."]
        subprocess.run(jar_command, check=True)
        print("ipc JAR 包生成成功")

    except subprocess.CalledProcessError as e:
        print(f"ipc 项目编译失败: {e}")
        sys.exit(1)

def compile_frontend():
    """编译前端项目"""
    print("正在安装前端依赖...")
    try:
        subprocess.run(["cnpm", "install"], check=True)
    except (subprocess.CalledProcessError, FileNotFoundError):
        subprocess.run(["npm", "install"], check=True)
    print("正在启动前端项目...")
    try:
        subprocess.run(["npm", "run", "start", "electron"], check=True)
        print("前端项目启动成功")
    except subprocess.CalledProcessError as e:
        print(f"前端项目启动失败: {e}")
        sys.exit(1)

def handle_dev_mode_jar(mode):
    jar_path = "jars/CookieByte-DevModeServerSocketJar.jar"
    cache_dir = os.path.join(os.getenv("HOME"), ".cache/CookieByte")
    cached_jar_path = os.path.join(cache_dir, "CookieByte-DevModeServerSocketJar.jar")

    if mode == "dev":
        if os.path.exists(cached_jar_path):
            os.makedirs("jars", exist_ok=True)
            shutil.copy2(cached_jar_path, jar_path)
            print("已从缓存复制 DevModeServerSocket JAR 文件")
        if os.path.exists(jar_path):
            try:
                subprocess.run(["java", "-jar", jar_path], check=True)
            except subprocess.CalledProcessError as e:
                print(f"运行 ServerSocket JAR 时出错: {e}")
                sys.exit(1)
        else:
            print("未找到 DevModeServerSocket JAR 文件，跳过运行")
    else:
        if os.path.exists(jar_path):
            os.makedirs(cache_dir, exist_ok=True)
            shutil.move(jar_path, cached_jar_path)
            print("已将 DevModeServerSocket JAR 文件移动到缓存目录")


if __name__ == '__main__':
    # 检查参数数量
    if len(sys.argv) < 2:
        print("您必须选择开发模式（dev）或发布模式（publish）")
        sys.exit(1)

    mode = sys.argv[1]
    if mode == "publish":
        modify_log4j_config()
    elif mode != "dev":
        print("无效的模式，请选择 dev 或 publish")
        sys.exit(1)
    if run_tests():
        compile_project()
        python_command = f"python3 -c 'import sys; from compile_script import handle_dev_mode_jar; handle_dev_mode_jar(\"{mode}\")'"
        print(f"请手动执行：{python_command} 在新的终端中")
        time.sleep(10)
        terminal_found = False
        compile_frontend()