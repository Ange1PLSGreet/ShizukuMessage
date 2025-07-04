import path from 'path';
import { spawn } from 'child_process';
import iconv from 'iconv-lite';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 执行普通命令
export const executeCommand = (command) => {
    return new Promise((resolve, reject) => {
        let output = '';
        let errorOutput = '';
        let cmd;
        if (process.platform === 'win32') {
            cmd = `cmd /c ${command}`;
            const child = spawn(cmd, { shell: true });

            child.stdout.on('data', (data) => {
                const decodedData = iconv.decode(data, 'gbk');
                output += decodedData;
            });

            child.stderr.on('data', (data) => {
                const decodedData = iconv.decode(data, 'gbk');
                errorOutput += decodedData;
            });

            child.on('close', (code) => {
                if (code !== 0) {
                    reject(new Error(errorOutput));
                } else {
                    resolve(output);
                }
            });

            child.on('error', (err) => {
                reject(err);
            });
        } else {
            cmd = command.split(' ');
            const child = spawn(cmd[0], cmd.slice(1), { encoding: 'utf8' });

            child.stdout.on('data', (data) => {
                output += data.toString();
            });

            child.stderr.on('data', (data) => {
                errorOutput += data.toString();
            });

            child.on('close', (code) => {
                if (code !== 0) {
                    reject(new Error(errorOutput));
                } else {
                    resolve(output);
                }
            });

            child.on('error', (err) => {
                reject(err);
            });
        }
    });
};

// 执行 Java 命令
export const executeJavaCommand = (ip, port) => {
    return new Promise((resolve, reject) => {
        let output = '';
        let errorOutput = '';
        // 修改 JAR 文件路径到 jars 目录
        const jarPath = path.join(__dirname, '..', 'jars', 'CookieByte--ClientSocket.jar');
        const javaCommand =  'java -Dfile.encoding=UTF-8 -cp target:org:jars/CookieByte--ClientSocket.jar org.cookiebyte.dev.server.IPCClient localhost 8080';
        console.log('即将执行的 Java 命令:', javaCommand);
        const child = spawn(javaCommand, { shell: true, encoding: 'utf8' });

        child.stdout.on('data', (data) => {
            output += data.toString();
        });

        child.stderr.on('data', (data) => {
            errorOutput += data.toString();
        });

        child.on('close', (code) => {
            if (code !== 0) {
                reject(new Error(errorOutput));
            } else {
                resolve(output);
            }
        });

        child.on('error', (err) => {
            reject(err);
        });
    });
};
