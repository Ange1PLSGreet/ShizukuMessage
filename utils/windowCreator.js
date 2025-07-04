import { BrowserWindow } from 'electron';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export const createAppWindow = () => {
    const preloadPath = path.join(__dirname, '..', 'src', 'preload.js');
    console.log('Preload 脚本路径:', preloadPath);

    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            preload: preloadPath,
            nodeIntegration: false,
            contextIsolation: true,
            sandbox: false
        }
    });

    win.loadURL('http://localhost:3000');
    win.webContents.openDevTools();
    return win;
};
