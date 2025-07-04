<template>
  <div>
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
    const port = '8080'; // 替换为实际端口号
    const result = await window.myAPI.executeJavaCommand(ip, port);
    javaCommandOutput.value = result;
  } catch (error) {
    javaCommandOutput.value = `Java 命令执行错误: ${error.message}`;
    console.error('Java 命令执行出错:', error);
  }
};
</script>