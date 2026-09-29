import { AlertTriangle, CheckCircle2, LoaderCircle, X } from 'lucide-react'
import { formatStatus } from '../api'

export function StatusBadge({ value }) {
  return <span className={`status status-${(value || 'unknown').toLowerCase().replaceAll('_', '-')}`}><span />{formatStatus(value)}</span>
}

export function PriorityBadge({ value }) {
  return <span className={`priority priority-${(value || 'medium').toLowerCase()}`}>{formatStatus(value)}</span>
}

export function Loader({ label = 'Loading CrimeWatch…' }) {
  return <div className="loader"><LoaderCircle className="spin" size={24} /><span>{label}</span></div>
}

export function EmptyState({ icon: Icon = AlertTriangle, title, text, action }) {
  return <div className="empty-state"><div className="empty-icon"><Icon size={26} /></div><h3>{title}</h3><p>{text}</p>{action}</div>
}

export function Alert({ type = 'error', children, onClose }) {
  return <div className={`alert alert-${type}`}>{type === 'success' ? <CheckCircle2 size={18} /> : <AlertTriangle size={18} />}<span>{children}</span>{onClose && <button onClick={onClose} aria-label="Close"><X size={16} /></button>}</div>
}

export function PageHeader({ eyebrow, title, subtitle, action }) {
  return <div className="page-header"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1>{subtitle && <p>{subtitle}</p>}</div>{action}</div>
}

export function MetricCard({ icon: Icon, label, value, note, tone = 'blue' }) {
  return <article className="metric-card"><div className={`metric-icon tone-${tone}`}><Icon size={21} /></div><div><span>{label}</span><strong>{value ?? '—'}</strong>{note && <small>{note}</small>}</div></article>
}

export function Modal({ title, children, onClose }) {
  return <div className="modal-backdrop" role="presentation" onMouseDown={e => e.target === e.currentTarget && onClose()}><section className="modal" role="dialog" aria-modal="true"><header><h2>{title}</h2><button className="icon-button" onClick={onClose}><X /></button></header>{children}</section></div>
}

