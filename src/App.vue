<template>
  <div>
    <input v-model="userInput" @keyup.enter="sendInput" placeholder="输入指令...">
    <button @click="runJavaCommand">运行 Java 命令</button>
    <pre>{{ javaCommandOutput }}</pre>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const javaCommandOutput = ref('');

const runJavaCommand = async () => {
  try {
    const ip = '127.0.0.1'; // 替换为实际 IP 地址
    const port = '4900'; // 替换为实际端口号
    const result = await window.myAPI.executeJavaCommand(ip, port);
    javaCommandOutput.value = result;
  } catch (error) {
    javaCommandOutput.value = `Java 命令执行错误: ${error.message}`;
    console.error('Java 命令执行出错:', error);
  }
};

// 新增输入处理逻辑
const userInput = ref('');

const sendInput = async () => {
  if (window.myAPI?.inputHandler) {
    window.myAPI.inputHandler.onInput(userInput.value);
    userInput.value = '';
  }
};
</script>