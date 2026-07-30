import { getComponent } from './componentMap'

/**
 * 将后端返回的 permission 树形数据递归转换为 Vue Router 路由数组
 * 作为 Layout 路由的 children 注入到 router 中
 *
 * @param {Array} permissions 后端 permission 树
 * @returns {Array} routes
 */
export function generateRoutes(permissions = []) {
  const routes = []
  for (const p of permissions) {
    const route = {
      path: p.path,
      name: p.name,
      component: getComponent(p.component),
      meta: { title: p.title, level: p.level, id: p.id }
    }
    if (p.children && p.children.length) {
      route.children = generateRoutes(p.children)
      // 一级菜单且有 children 时，自动添加 redirect 到第一个子路径
      if (route.children[0]?.path) {
        let first = route.children[0].path
        if (!first.startsWith('/')) first = `/${first}`
        route.redirect = `${p.path}${first.startsWith('/') ? first : '/' + first}`
      }
    }
    routes.push(route)
  }
  return routes
}

/**
 * 将 permission 树转换为侧边栏菜单树（用于渲染 el-menu / el-sub-menu）
 * @param {Array} permissions 后端 permission 树
 * @returns {Array} menus
 */
export function generateMenus(permissions = []) {
  const menus = []
  for (const p of permissions) {
    const menu = {
      id: p.id,
      path: p.path,
      title: p.title,
      name: p.name,
      level: p.level
    }
    if (p.children && p.children.length) {
      menu.children = generateMenus(p.children)
      // 叶子节点：只有一个默认子节点且无 grandchildren 时，直接让父菜单指向子菜单路径（更友好的跳转）
      if (menu.children.length === 1 && !menu.children[0].children?.length) {
        menu.path = menu.children[0].path
      }
    }
    menus.push(menu)
  }
  return menus
}
