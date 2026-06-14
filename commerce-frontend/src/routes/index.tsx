import { createBrowserRouter } from 'react-router-dom';
import PublicLayout from '../layouts/PublicLayout';
import CustomerLayout from '../layouts/CustomerLayout';
import AdminLayout from '../layouts/AdminLayout';
import HomePage from '../pages/HomePage';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import ProductsPage from '../pages/ProductsPage';
import ProductDetailPage from '../pages/ProductDetailPage';
import CartPage from '../pages/CartPage';
import CheckoutPage from '../pages/CheckoutPage';
import AccountPage from '../pages/AccountPage';
import OrdersPage from '../pages/OrdersPage';
import WishlistPage from '../pages/WishlistPage';
import RecommendationsPage from '../pages/RecommendationsPage';
import AdminDashboardPage from '../pages/AdminDashboardPage';
import NotFoundPage from '../pages/NotFoundPage';
import ProtectedRoute from '../components/ProtectedRoute';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <PublicLayout><HomePage /></PublicLayout>,
  },
  { path: '/login', element: <PublicLayout><LoginPage /></PublicLayout> },
  { path: '/register', element: <PublicLayout><RegisterPage /></PublicLayout> },
  { path: '/forgot-password', element: <PublicLayout><div className="p-8 text-white">Forgot Password</div></PublicLayout> },
  { path: '/reset-password', element: <PublicLayout><div className="p-8 text-white">Reset Password</div></PublicLayout> },
  { path: '/products', element: <PublicLayout><ProductsPage /></PublicLayout> },
  { path: '/products/:slug', element: <PublicLayout><ProductDetailPage /></PublicLayout> },
  { path: '/account', element: <ProtectedRoute><CustomerLayout><AccountPage /></CustomerLayout></ProtectedRoute> },
  { path: '/account/profile', element: <ProtectedRoute><CustomerLayout><AccountPage /></CustomerLayout></ProtectedRoute> },
  { path: '/account/orders', element: <ProtectedRoute><CustomerLayout><OrdersPage /></CustomerLayout></ProtectedRoute> },
  { path: '/account/wishlist', element: <ProtectedRoute><CustomerLayout><WishlistPage /></CustomerLayout></ProtectedRoute> },
  { path: '/cart', element: <ProtectedRoute><CustomerLayout><CartPage /></CustomerLayout></ProtectedRoute> },
  { path: '/checkout', element: <ProtectedRoute><CustomerLayout><CheckoutPage /></CustomerLayout></ProtectedRoute> },
  { path: '/recommendations', element: <ProtectedRoute><CustomerLayout><RecommendationsPage /></CustomerLayout></ProtectedRoute> },
  { path: '/admin', element: <ProtectedRoute><AdminLayout><AdminDashboardPage /></AdminLayout></ProtectedRoute> },
  { path: '*', element: <PublicLayout><NotFoundPage /></PublicLayout> },
]);
