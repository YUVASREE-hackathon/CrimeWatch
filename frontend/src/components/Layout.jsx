import { useState } from 'react'
import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import { Activity, BarChart3, Bell, ChevronRight, ClipboardList, FilePlus2, Fingerprint, Gauge, LogOut, Menu, Search, Settings, ShieldCheck, UserCircle, Users, X } from 'lucide-react'
import { useAuth } from '../auth'

const navByRole = {
  CITIZEN: [
    ['/app', Gauge, 'Dashboard'], ['/app/reports', ClipboardList, 'My Reports'], ['/app/report/new', FilePlus2, 'Report Incident'], ['/app/notifications', Bell, 'Notifications'], ['/app/profile', UserCircle, 'Profile']
  ],
  OFFICER: [
    ['/app', Gauge, 'Dashboard'], ['/app/reports', ClipboardList, 'Assigned Cases'], ['/app/analytics', BarChart3, 'Analytics'], ['/app/notifications', Bell, 'Notifications'], ['/app/profile', UserCircle, 'Profile']
  ],
  ADMIN: [
    ['/app', Gauge, 'Dashboard'], ['/app/reports', ClipboardList, 'All Reports'], ['/app/users', Users, 'Users & Officers'], ['/app/analytics', BarChart3, 'Analytics'], ['/app/audit', Activity, 'Audit Logs'], ['/app/system', Settings, 'System'], ['/app/profile', UserCircle, 'Profile']
  ]
}

export default function Layout({ children }) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [open, setOpen] = useState(false)
  const nav = navByRole[user?.role] || navByRole.CITIZEN
  const signOut = () => { logout(); navigate('/') }
  const crumb = nav.find(([path]) => path === location.pathname)?.[2] || (location.pathname.includes('/reports/') ? 'Case Details' : 'CrimeWatch')
  return <div className="app-shell">
    <aside className={`sidebar ${open ? 'sidebar-open' : ''}`}>
      <div className="brand"><span className="brand-mark"><Fingerprint /></span><span><strong>CrimeWatch</strong><small>Command & Response</small></span><button className="mobile-close" onClick={() => setOpen(false)}><X /></button></div>
      <div className="role-chip"><ShieldCheck size={16} /><span>{user?.role === 'OFFICER' ? 'Police Officer' : user?.role}</span></div>
      <nav>{nav.map(([path, Icon, label]) => <NavLink key={path} to={path} end={path === '/app'} onClick={() => setOpen(false)}><Icon size={19} /><span>{label}</span><ChevronRight className="nav-arrow" size={15} /></NavLink>)}</nav>
      <div className="sidebar-foot"><div className="system-online"><span /> All systems operational</div><button onClick={signOut}><LogOut size={18} /> Sign out</button></div>
    </aside>
    {open && <button className="sidebar-scrim" aria-label="Close menu" onClick={() => setOpen(false)} />}
    <main className="app-main">
      <header className="topbar"><button className="menu-button" onClick={() => setOpen(true)}><Menu /></button><div className="breadcrumb"><span>Workspace</span><ChevronRight size={14} /><strong>{crumb}</strong></div><div className="top-actions"><button className="top-search" onClick={() => navigate('/app/reports')}><Search size={17} /><span>Search reports</span><kbd>⌘ K</kbd></button><button className="notification-button" onClick={() => navigate('/app/notifications')}><Bell size={19} /><i /></button><div className="user-pill"><span>{user?.fullName?.split(' ').map(x => x[0]).slice(0, 2).join('')}</span><div><strong>{user?.fullName}</strong><small>{user?.email}</small></div></div></div></header>
      <div className="page-content">{children}</div>
    </main>
  </div>
}
