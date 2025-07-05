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
        # 添加 shell=True 参数确保能正确找到系统路径
        test_command = subprocess.run(["mvn", "test"], 
                                    capture_output=True, 
                                    text=True, 
                                    check=True,
                                    shell=True)  # 添加 shell 参数
        print("测试通过")
        return True
    except subprocess.CalledProcessError as e:
        print("测试失败，终止编译")
        print("错误信息:")
        print(e.stderr)
        # 添加 Maven 安装提示
        print("\n请确认已正确安装 Maven 并配置环境变量")
        print("官方下载地址：https://maven.apache.org/download.cgi")
        return False
    except FileNotFoundError:  # 添加文件未找到的专门处理
        print("错误：未找到 Maven 执行文件 (mvn)")
        print("请确认 Maven 已安装并添加到系统 PATH 环境变量")
        return False

def compile_project():
    """编译项目"""
    print("正在编译主项目...")
    try:
        subprocess.run(["mvn", "clean", "package"], check=True, shell=True)  # 添加 shell 参数
        print("主项目编译成功")
    except subprocess.CalledProcessError as e:
        print(f"主项目编译失败: {e}")
        sys.exit(1)
    except FileNotFoundError:
        print("错误：未找到 Maven 执行文件 (mvn)")
        print("请确认 Maven 已安装并添加到系统 PATH 环境变量")
        sys.exit(1)

    print("正在编译 ipc 目录下的项目...")
    try:
        ipc_src_dir = "src/main/java/org/cookiebyte/dev/ipc"
        output_dir = "target/classes/org/cookiebyte/dev/ipc"
        jar_output_dir = "target/jars"
        jar_name = "CookieByte_IPC.jar"
        dependency_dir = "target/dependency-jars"

        # 创建输出目录
        # 使用Python原生目录创建（Windows兼容）
        os.makedirs(output_dir, exist_ok=True)
        os.makedirs(jar_output_dir, exist_ok=True)

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
        os.makedirs(manifest_dest, exist_ok=True)
        if os.path.exists(manifest_src):
            shutil.copy2(manifest_src, manifest_dest)  # 使用shutil替代cp命令
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
        # 添加 shell=True 并处理 Windows 路径空格
        subprocess.run(["cnpm", "install"], check=True, shell=True)
    except (subprocess.CalledProcessError, FileNotFoundError):
        try:
            # 回退到 npm 并添加 shell=True
            subprocess.run(["npm", "install"], check=True, shell=True)
        except subprocess.CalledProcessError as e:
            print(f"前端依赖安装失败: {e}")
            sys.exit(1)
    print("正在启动前端项目...")
    try:
        # 添加 shell=True 参数
        subprocess.run(["npm", "run", "start", "electron"], check=True, shell=True)
        print("前端项目启动成功")
    except subprocess.CalledProcessError as e:
        print(f"前端项目启动失败: {e}")
        sys.exit(1)

def handle_dev_mode_jar(mode):
    # 修改为Windows兼容的路径获取方式
    cache_dir = os.path.join(os.getenv("USERPROFILE"), ".cache", "CookieByte")  # 使用USERPROFILE替代HOME
    cached_jar_path = os.path.join(cache_dir, "CookieByte-DevModeServerSocketJar.jar")
    jar_path = "jars/CookieByte-DevModeServerSocketJar.jar"

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
        # 修正引号转义问题
        python_command = f'start cmd /k "python -c \"from compile_script import handle_dev_mode_jar; handle_dev_mode_jar(\'{mode}\')\""'
        print("请执行以下命令（直接复制到CMD窗口）：")
        print(python_command)
        time.sleep(3)
        compile_frontend()