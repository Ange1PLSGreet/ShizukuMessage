import { contextBridge, ipcRenderer } from 'electron';

try {
    console.log('开始执行 preload.js');
    console.log('准备注入 window.myAPI');
    contextBridge.exposeInMainWorld('myAPI', {
        // 确保方法名正确暴露
        executeJavaCommand: (ip, port) => {
            return ipcRenderer.invoke('execute-java-command', ip, port);
        },
        // 保留之前的方法
        executeCommand: (command) => {
            return ipcRenderer.invoke('execute-command', command);
        },
        // 添加 getPlatform 方法
        getPlatform: () => {
            return process.platform;
        }
    });
    console.log('window.myAPI 注入成功');
} catch (error) {
    console.error('preload.js 执行出错:', error);
    console.error('错误堆栈:', error.stack);
}
