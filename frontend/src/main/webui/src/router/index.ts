// Composables
import { createRouter, createWebHistory } from 'vue-router';
import Customers from '@/components/Customers.vue';
import Dashboard from '@/components/Dashboard.vue';
import Purchase from '@/components/Purchase.vue';
import Products from '@/components/Products.vue';

const routes = [
  {
    path: '/',
    component: Dashboard,
    name: 'Dashboard'
  },
  {
    path: '/customers',
    component: Customers,
    name: 'Customers'
  },
  {
    path: '/purchase',
    component: Purchase, 
    name: 'Purchase'
  }, 
  {
    path: '/products', 
    component: Products, 
    name: 'Products'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
