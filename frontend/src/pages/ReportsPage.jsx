import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { ChevronLeft, ChevronRight, FilePlus2, Search, SlidersHorizontal } from 'lucide-react'
import { api } from '../api'
import { useAuth } from '../auth'
import { Alert, EmptyState, Loader, PageHeader } from '../components/Ui'
import { ReportTable } from './Dashboard'

export default function ReportsPage() {
  const { user } = useAuth()
  const [data, setData] = useState(null)
  const [filters, setFilters] = useState({ search: '', status: '', priority: '', page: 0 })
  const [query, setQuery] = useState('')
  const [error, setError] = useState('')
  const load = () => { const p = new URLSearchParams({ page: filters.page, size: 10 }); if (query) p.set('search', query); if (filters.status) p.set('status', filters.status); if (filters.priority) p.set('priority', filters.priority); api(`/reports?${p}`).then(setData).catch(e => setError(e.message)) }
  useEffect(load, [filters.page, filters.status, filters.priority, query])
  const submit = e => { e.preventDefault(); setFilters(f => ({ ...f, page: 0 })); setQuery(filters.search) }
  if (!data && !error) return <Loader label="Loading case records…" />
  return <><PageHeader eyebrow="Case management" title={user.role === 'CITIZEN' ? 'My reports' : user.role === 'OFFICER' ? 'Assigned cases' : 'All crime reports'} subtitle="Search, filter and inspect complete incident records." action={user.role === 'CITIZEN' && <Link className="button button-primary" to="/app/report/new"><FilePlus2 size={18}/> Report incident</Link>} />{error && <Alert>{error}</Alert>}
    <section className="panel filters-panel"><form onSubmit={submit} className="search-box"><Search size={18}/><input value={filters.search} onChange={e => setFilters({...filters, search:e.target.value})} placeholder="Search report ID, title, city or location"/><button>Search</button></form><div className="filter-row"><span><SlidersHorizontal size={16}/> Filters</span><select value={filters.status} onChange={e => setFilters({...filters,status:e.target.value,page:0})}><option value="">All statuses</option>{['SUBMITTED','UNDER_REVIEW','ASSIGNED','UNDER_INVESTIGATION','RESOLVED','CLOSED'].map(s=><option key={s}>{s}</option>)}</select><select value={filters.priority} onChange={e => setFilters({...filters,priority:e.target.value,page:0})}><option value="">All priorities</option>{['LOW','MEDIUM','HIGH','CRITICAL'].map(s=><option key={s}>{s}</option>)}</select><button className="clear-filter" onClick={() => {setFilters({search:'',status:'',priority:'',page:0});setQuery('')}} type="button">Clear filters</button></div></section>
    <section className="panel report-list-panel"><div className="panel-head"><div><span className="eyebrow">Verified records</span><h2>{data?.totalElements || 0} reports found</h2></div><span className="page-count">Page {(data?.number || 0)+1} of {Math.max(data?.totalPages || 1,1)}</span></div>{data?.content?.length ? <ReportTable reports={data.content}/> : <EmptyState title="No matching reports" text="Try changing your search terms or filters."/>}<div className="pagination"><button disabled={data?.first} onClick={() => setFilters({...filters,page:filters.page-1})}><ChevronLeft/> Previous</button><button disabled={data?.last} onClick={() => setFilters({...filters,page:filters.page+1})}>Next <ChevronRight/></button></div></section>
  </>
}
