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
        const jarPath = path.join(__dirname, '..', 'jars', 'CookieByte_ClientSocket.jar');
        const classPath = `target:org:${jarPath}`;
        const javaCommand = [
            'java',
            '-Dfile.encoding=UTF-8',
            '-cp',
            classPath,
            'org.cookiebyte.dev.server.IPCClient',
            ip,
            port
        ];

        console.log('即将执行的 Java 命令:', javaCommand.join(' '));
        const child = spawn(javaCommand[0], javaCommand.slice(1), { encoding: 'utf8' });

        child.stdout.on('data', (data) => {
            output += data.toString();
        });

        child.stderr.on('data', (data) => {
            errorOutput += data.toString();
            // 实时打印错误信息
            console.error('Java 命令执行过程中出错:', data.toString());
        });

        child.on('close', (code) => {
            if (code !== 0) {
                const error = new Error(errorOutput);
                console.error('Java 命令执行结束时出错:', error);
                reject(error);
            } else {
                resolve(output);
            }
        });

        child.on('error', (err) => {
            console.error('Java 进程启动出错:', err);
            reject(err);
        });
    });
};