<template>
  <div>
    <Header></Header>
    <el-container>
      <Aside></Aside>
      <el-main style="height: 635px">
        <keep-alive :include="['OrderNow']">
          <router-view></router-view>
        </keep-alive>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Header from "./Header";
import Aside from "./Aside";

export default {
  name: "Home",
  components: {
    Header,
    Aside
  }
}
</script>