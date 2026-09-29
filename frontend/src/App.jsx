import { Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider, useAuth } from './auth'
import Layout from './components/Layout'
import Landing from './pages/Landing'
import AuthPage from './pages/AuthPage'
import Dashboard from './pages/Dashboard'
import ReportsPage from './pages/ReportsPage'
import ReportForm from './pages/ReportForm'
import ReportDetail from './pages/ReportDetail'
import Analytics from './pages/Analytics'
import Notifications from './pages/Notifications'
import Profile from './pages/Profile'
import { AuditPage, SystemPage, UsersPage } from './pages/AdminPages'

function Protected({ children, roles }) {
  const { user } = useAuth()
  if (!user || !localStorage.getItem('crimewatch_token')) return <Navigate to="/login" replace />
  if (roles && !roles.includes(user.role)) return <Navigate to="/app" replace />
  return <Layout>{children}</Layout>
}

function PublicOnly({ children }) { const { user } = useAuth(); return user ? <Navigate to="/app" replace /> : children }

export default function App(){return <AuthProvider><Routes>
  <Route path="/" element={<Landing/>}/>
  <Route path="/login" element={<PublicOnly><AuthPage mode="login"/></PublicOnly>}/>
  <Route path="/register" element={<PublicOnly><AuthPage mode="register"/></PublicOnly>}/>
  <Route path="/app" element={<Protected><Dashboard/></Protected>}/>
  <Route path="/app/reports" element={<Protected><ReportsPage/></Protected>}/>
  <Route path="/app/report/new" element={<Protected roles={['CITIZEN']}><ReportForm/></Protected>}/>
  <Route path="/app/reports/:id" element={<Protected><ReportDetail/></Protected>}/>
  <Route path="/app/analytics" element={<Protected roles={['ADMIN','OFFICER']}><Analytics/></Protected>}/>
  <Route path="/app/notifications" element={<Protected><Notifications/></Protected>}/>
  <Route path="/app/profile" element={<Protected><Profile/></Protected>}/>
  <Route path="/app/users" element={<Protected roles={['ADMIN']}><UsersPage/></Protected>}/>
  <Route path="/app/audit" element={<Protected roles={['ADMIN']}><AuditPage/></Protected>}/>
  <Route path="/app/system" element={<Protected roles={['ADMIN']}><SystemPage/></Protected>}/>
  <Route path="*" element={<Navigate to="/" replace/>}/>
</Routes></AuthProvider>}
