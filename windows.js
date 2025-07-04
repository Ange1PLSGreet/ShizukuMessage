import { app, BrowserWindow, ipcMain } from 'electron';
import path from 'path';
import { fileURLToPath } from 'url';
import { executeCommand, executeJavaCommand } from './utils/commandExecutor.js';
import { createAppWindow } from './utils/windowCreator.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 处理执行普通命令的事件
ipcMain.handle('execute-command', async (event, command) => {
    try {
        return await executeCommand(command);
    } catch (error) {
        throw error;
    }
});

// 处理执行 Java 命令的事件
ipcMain.handle('execute-java-command', async (event, ip, port) => {
    try {
        return await executeJavaCommand(ip, port);
    } catch (error) {
        throw error;
    }
});

app.whenReady().then(() => {
    createAppWindow();

    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) {
            createAppWindow();
        }
    });
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});