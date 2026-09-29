import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { Activity, ArrowRight, CheckCircle2, CircleAlert, ClipboardList, Clock3, FilePlus2, MapPin, ShieldAlert, Siren } from 'lucide-react'
import { Area, AreaChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { api, formatDate } from '../api'
import { useAuth } from '../auth'
import { Alert, Loader, MetricCard, PageHeader, PriorityBadge, StatusBadge } from '../components/Ui'

const COLORS = ['#2563eb', '#f59e0b', '#8b5cf6', '#06b6d4', '#22c55e', '#64748b']

export default function Dashboard() {
  const { user } = useAuth()
  const [reports, setReports] = useState([])
  const [analytics, setAnalytics] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  useEffect(() => { Promise.all([api('/reports?size=100'), user.role === 'CITIZEN' ? Promise.resolve(null) : api('/analytics/summary')])
    .then(([r, a]) => { setReports(r.content); setAnalytics(a) }).catch(e => setError(e.message)).finally(() => setLoading(false)) }, [user.role])
  const counts = useMemo(() => ({ total: reports.length, pending: reports.filter(r => ['SUBMITTED', 'UNDER_REVIEW'].includes(r.status)).length, active: reports.filter(r => ['ASSIGNED', 'UNDER_INVESTIGATION'].includes(r.status)).length, resolved: reports.filter(r => ['RESOLVED', 'CLOSED'].includes(r.status)).length }), [reports])
  if (loading) return <Loader label="Preparing your operational dashboard…" />
  const isCitizen = user.role === 'CITIZEN'
  const display = analytics || counts
  return <>
    <PageHeader eyebrow={`${user.role === 'OFFICER' ? 'Police operations' : user.role === 'ADMIN' ? 'Administrative command' : 'Citizen portal'} · Live overview`} title={`Good ${greeting()}, ${user.fullName.split(' ')[0]}`} subtitle={isCitizen ? 'Track your reports and stay informed about every case update.' : 'Here is the current operational picture across CrimeWatch.'} action={isCitizen && <Link className="button button-primary" to="/app/report/new"><FilePlus2 size={18} /> Report incident</Link>} />
    {error && <Alert>{error}</Alert>}
    <div className="metrics-grid">
      <MetricCard icon={ClipboardList} label={isCitizen ? 'Your reports' : 'Total reports'} value={isCitizen ? counts.total : display.totalReports} note="All recorded cases" tone="blue" />
      <MetricCard icon={Clock3} label="Pending review" value={isCitizen ? counts.pending : (display.submitted + display.underReview)} note="Awaiting next action" tone="amber" />
      <MetricCard icon={Activity} label="Active cases" value={isCitizen ? counts.active : (display.assigned + display.underInvestigation)} note="Currently progressing" tone="purple" />
      <MetricCard icon={CheckCircle2} label="Resolved & closed" value={isCitizen ? counts.resolved : (display.resolved + display.closed)} note="Completed outcomes" tone="green" />
    </div>
    {isCitizen ? <CitizenOverview reports={reports} /> : <OperationsOverview analytics={analytics} />}
    <section className="panel recent-panel"><div className="panel-head"><div><span className="eyebrow">Most recently updated</span><h2>{isCitizen ? 'Your reports' : 'Active report queue'}</h2></div><Link to="/app/reports">View all <ArrowRight size={16} /></Link></div><ReportTable reports={reports.slice(0, 6)} /></section>
  </>
}

function CitizenOverview({ reports }) {
  const latest = reports[0]
  return <div className="dashboard-split"><section className="panel progress-panel"><div className="panel-head"><div><span className="eyebrow">Latest case</span><h2>Case progress</h2></div>{latest && <StatusBadge value={latest.status} />}</div>{latest ? <><div className="featured-case"><div><strong>{latest.publicId}</strong><h3>{latest.title}</h3><span><MapPin size={15} /> {latest.area || latest.location}, {latest.city}</span></div><Link className="button button-secondary" to={`/app/reports/${latest.publicId}`}>View case</Link></div><StatusTrack current={latest.status} /></> : <div className="first-report"><Siren /><h3>No incidents reported</h3><p>Your submitted reports will appear here.</p><Link className="button button-primary" to="/app/report/new">Report an incident</Link></div>}</section><section className="panel safety-panel"><div className="safety-icon"><ShieldAlert /></div><span className="eyebrow">Safety guidance</span><h2>In immediate danger?</h2><p>CrimeWatch is not an emergency dispatch service. Contact your local emergency number for an active threat or medical emergency.</p><div className="tip"><CircleAlert /><span>When safe, preserve evidence and avoid disturbing the incident location.</span></div></section></div>
}

function OperationsOverview({ analytics }) {
  return <div className="dashboard-split wide-chart"><section className="panel chart-panel"><div className="panel-head"><div><span className="eyebrow">Reporting velocity</span><h2>Monthly incident trend</h2></div><span className="chart-note">Last 12 months</span></div><div className="chart-wrap"><ResponsiveContainer width="100%" height="100%"><AreaChart data={analytics?.monthly || []}><defs><linearGradient id="crimeFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#3b82f6" stopOpacity={.35}/><stop offset="100%" stopColor="#3b82f6" stopOpacity={0}/></linearGradient></defs><CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e5eaf1"/><XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fontSize:11, fill:'#738095'}}/><YAxis axisLine={false} tickLine={false} tick={{fontSize:11, fill:'#738095'}} allowDecimals={false}/><Tooltip/><Area type="monotone" dataKey="value" stroke="#2563eb" strokeWidth={3} fill="url(#crimeFill)"/></AreaChart></ResponsiveContainer></div></section><section className="panel distribution-panel"><div className="panel-head"><div><span className="eyebrow">Case lifecycle</span><h2>Status distribution</h2></div></div><div className="donut-wrap"><ResponsiveContainer width="54%" height={220}><PieChart><Pie data={analytics?.statuses || []} dataKey="value" nameKey="name" innerRadius={58} outerRadius={84} paddingAngle={3}>{(analytics?.statuses || []).map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}</Pie><Tooltip/></PieChart></ResponsiveContainer><div className="legend">{(analytics?.statuses || []).map((x, i) => <span key={x.name}><i style={{background: COLORS[i % COLORS.length]}} />{x.name.replaceAll('_',' ')}<b>{x.value}</b></span>)}</div></div></section></div>
}

export function ReportTable({ reports }) {
  return <div className="table-wrap"><table><thead><tr><th>Report</th><th>Category</th><th>Location</th><th>Date</th><th>Priority</th><th>Status</th><th /></tr></thead><tbody>{reports.map(r => <tr key={r.publicId}><td><b className="report-id">{r.publicId}</b><span className="cell-subtitle">{r.title}</span></td><td>{r.category.name}</td><td><span className="location-cell"><MapPin size={14}/>{r.city}</span></td><td>{formatDate(r.incidentDate)}</td><td><PriorityBadge value={r.priority}/></td><td><StatusBadge value={r.status}/></td><td><Link className="row-action" to={`/app/reports/${r.publicId}`}>Open <ArrowRight size={14}/></Link></td></tr>)}</tbody></table></div>
}

function StatusTrack({ current }) { const stages = ['SUBMITTED','UNDER_REVIEW','ASSIGNED','UNDER_INVESTIGATION','RESOLVED','CLOSED']; const index = stages.indexOf(current); return <div className="status-track">{stages.map((s,i) => <div key={s} className={i < index ? 'done' : i === index ? 'current' : ''}><i>{i < index ? '✓' : i + 1}</i><span>{s.replaceAll('_',' ')}</span></div>)}</div> }
function greeting() { const h = new Date().getHours(); return h < 12 ? 'morning' : h < 17 ? 'afternoon' : 'evening' }

