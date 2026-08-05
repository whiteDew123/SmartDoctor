<template>
  <div class="home-page">
    <!-- ===== 轮播图 ===== -->
    <el-carousel
      class="home-banner"
      :interval="4000"
      height="420px"
      arrow="hover"
      indicator-position="outside"
    >
      <el-carousel-item v-for="(item, idx) in banners" :key="idx">
        <div class="banner-slide">
          <img :src="item.image" class="banner-img" alt="" />
          <div class="banner-overlay"></div>
          <div class="banner-text">
            <h2 class="banner-title">{{ item.title }}</h2>
            <p v-if="item.subTitle" class="banner-sub">{{ item.subTitle }}</p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- ===== 六大模块管理卡片 ===== -->
    <section class="modules">
      <div class="modules-inner">
        <div
          v-for="mod in modules"
          :key="mod.title"
          class="mod-card"
          @click="toModule(mod)"
        >
          <div class="mod-thumb">
            <img :src="mod.image" alt="" />
          </div>
          <div class="mod-title">{{ mod.title }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import banner1 from '@/assets/images/hUlOtf.jpg'
import banner2 from '@/assets/images/hUlLAP.png'
import banner3 from '@/assets/images/hUl7nA.jpg'
import banner4 from '@/assets/images/hUlb7t.jpg'
import banner5 from '@/assets/images/hUlH0I.png'
import modImg1 from '@/assets/images/hU0yG9.jpg'
import modImg2 from '@/assets/images/hU0KDf.jpg'
import modImg3 from '@/assets/images/hUBref.jpg'
import modImg4 from '@/assets/images/hUDHDP.jpg'
import modImg5 from '@/assets/images/hUB2Wj.jpg'
import modImg6 from '@/assets/images/hUDYn0.jpg'

const router = useRouter()

const banners = [
  {
    title: '关爱健康，从预防开始',
    subTitle: 'CARING FOR HEALTH STARTS FROM PREVENTION',
    image: banner1
  },
  {
    title: '智能医疗，开启未来',
    subTitle: 'SMART MEDICINE, OPENING THE FUTURE',
    image: banner2
  },
  {
    title: '传承医疗，服务大众',
    subTitle: 'INHERITING MEDICINE, SERVING THE PUBLIC',
    image: banner3
  },
  {
    title: '专业医疗，守护生命',
    subTitle: 'PROFESSIONAL MEDICINE, GUARDING LIFE',
    image: banner4
  },
  {
    title: '医疗呵护，伴你成长',
    subTitle: 'MEDICAL CARE, GROWING WITH YOU',
    image: banner5
  }
]

const modules = [
  { title: '基础信息管理', image: modImg1, path: '/system/account' },
  { title: '药品信息管理', image: modImg2, path: '/drug/list' },
  { title: '医保政策管理', image: modImg3, path: '/policy/list' },
  { title: '医药公司政策管理', image: modImg4, path: '/company/policy' },
  { title: '医生信息管理', image: modImg5, path: '/doctor' },
  { title: '必备材料管理', image: modImg6, path: '/home' }
]

const toModule = (mod) => {
  router.push(mod.path)
}
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  min-height: 100%;
  padding: 0;
  background: #f2f4f7;
}

/* ========= 轮播 ========= */
.home-banner {
  width: 100%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  background: #fff;

  .banner-slide {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;

    .banner-img {
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .banner-overlay {
      position: absolute;
      inset: 0;
      background: linear-gradient(
        90deg,
        rgba(255, 255, 255, 0.85) 0%,
        rgba(255, 255, 255, 0.4) 40%,
        rgba(255, 255, 255, 0.15) 100%
      );
    }

    .banner-text {
      position: relative;
      z-index: 2;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding-left: 8%;
      max-width: 60%;

      .banner-title {
        margin: 0 0 16px;
        font-size: 56px;
        font-weight: 800;
        letter-spacing: 4px;
        color: #13759d;
        line-height: 1.15;
        text-shadow: 1px 2px 4px rgba(255, 255, 255, 0.8);
      }

      .banner-sub {
        margin: 0;
        font-size: 20px;
        letter-spacing: 3px;
        color: #2fb79b;
        font-weight: 600;
      }
    }
  }
}

/* ========= 模块卡片 ========= */
.modules {
  width: 100%;
  padding: 32px 0 40px;

  .modules-inner {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 28px;
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 24px;
  }
}

.mod-card {
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 24px rgba(0, 0, 0, 0.1);
  }

  .mod-thumb {
    height: 160px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
  }

  &:hover .mod-thumb img {
    transform: scale(1.05);
  }

  .mod-title {
    padding: 14px 18px 16px;
    font-size: 15px;
    font-weight: 600;
    color: #1f2937;
    letter-spacing: 1px;
    border-top: 1px solid #f0f0f0;
    text-align: center;
  }
}

/* 响应式 */
@media (max-width: 1300px) {
  .modules .modules-inner {
    grid-template-columns: repeat(3, 1fr);
  }
  .home-banner .banner-slide .banner-text .banner-title {
    font-size: 44px;
  }
}
@media (max-width: 860px) {
  .modules .modules-inner {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-banner .banner-slide .banner-text .banner-title {
    font-size: 32px;
  }
}
</style>