import { useEffect, useState } from 'react'
import { Bell, CheckCheck, Clock3 } from 'lucide-react'
import { api, formatDate } from '../api'
import { EmptyState, Loader, PageHeader } from '../components/Ui'

export default function Notifications(){const[data,setData]=useState(null);const load=()=>api('/notifications').then(setData);useEffect(()=>{load()},[]);const mark=async id=>{await api(`/notifications/${id}/read`,{method:'PUT'});load()};if(!data)return <Loader label="Loading notifications…"/>;return <><PageHeader eyebrow="Case communication" title="Notifications" subtitle="Status updates and assignments from across your CrimeWatch workspace." action={<span className="unread-summary"><Bell/> {data.filter(x=>!x.read).length} unread</span>}/><section className="panel notification-list">{data.length?data.map(n=><article className={n.read?'':'unread'} key={n.id}><span className="notification-icon"><Bell/></span><div><b>{n.reportId||'CrimeWatch update'}</b><p>{n.message}</p><small><Clock3/> {formatDate(n.createdAt)}</small></div>{!n.read&&<button onClick={()=>mark(n.id)}><CheckCheck/> Mark read</button>}</article>):<EmptyState icon={Bell} title="You're all caught up" text="New case updates will appear here."/>}</section></>}

