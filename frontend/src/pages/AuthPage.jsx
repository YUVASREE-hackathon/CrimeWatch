import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { ArrowLeft, ArrowRight, Eye, EyeOff, Fingerprint, LockKeyhole, Mail, ShieldCheck, UserRound } from 'lucide-react'
import { useAuth } from '../auth'
import { Alert } from '../components/Ui'

const demos = [
  { role: 'Citizen', email: 'citizen@crimewatch.demo', password: 'Citizen@123' },
  { role: 'Officer', email: 'officer1@crimewatch.demo', password: 'Officer@123' },
  { role: 'Admin', email: 'admin@crimewatch.demo', password: 'Admin@123' }
]

export default function AuthPage({ mode }) {
  const isRegister = mode === 'register'
  const { login, register } = useAuth()
  const navigate = useNavigate()
  const [show, setShow] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [form, setForm] = useState({ fullName: '', email: '', password: '', phone: '', address: '' })
  const change = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => { e.preventDefault(); setLoading(true); setError(''); try { await (isRegister ? register(form) : login({ email: form.email, password: form.password })); navigate('/app') } catch (err) { setError(err.message) } finally { setLoading(false) } }
  const useDemo = demo => setForm({ ...form, email: demo.email, password: demo.password })

  return <div className="auth-page"><aside className="auth-aside"><Link className="brand" to="/"><span className="brand-mark"><Fingerprint /></span><span><strong>CrimeWatch</strong><small>Command & Response</small></span></Link><div className="auth-message"><span className="eyebrow">Trusted incident management</span><h1>{isRegister ? 'Your voice can make your community safer.' : 'Welcome back to the response network.'}</h1><p>{isRegister ? 'Create a secure citizen account to report incidents and follow every meaningful update.' : 'Sign in to continue reporting, investigating, or coordinating response.'}</p></div><div className="auth-assurance"><ShieldCheck /><div><b>Protected by role-based security</b><span>Passwords are hashed and access is validated on every request.</span></div></div></aside>
    <main className="auth-main"><Link to="/" className="back-link"><ArrowLeft size={16} /> Back to home</Link><div className="auth-card"><div className="auth-title"><span className="auth-symbol"><LockKeyhole /></span><h2>{isRegister ? 'Create citizen account' : 'Sign in to CrimeWatch'}</h2><p>{isRegister ? 'Enter your details to begin secure reporting.' : 'Use your registered account or a demo identity.'}</p></div>{error && <Alert>{error}</Alert>}<form onSubmit={submit}>
      {isRegister && <Field icon={UserRound} label="Full name"><input name="fullName" value={form.fullName} onChange={change} minLength="2" required placeholder="Enter your full name" /></Field>}
      <Field icon={Mail} label="Email address"><input name="email" type="email" value={form.email} onChange={change} required placeholder="name@example.com" autoComplete="email" /></Field>
      <Field icon={LockKeyhole} label="Password"><div className="password-field"><input name="password" type={show ? 'text' : 'password'} value={form.password} onChange={change} minLength="8" required placeholder="Minimum 8 characters" autoComplete={isRegister ? 'new-password' : 'current-password'} /><button type="button" onClick={() => setShow(!show)} aria-label="Toggle password">{show ? <EyeOff /> : <Eye />}</button></div></Field>
      {isRegister && <><Field label="Phone number"><input name="phone" value={form.phone} onChange={change} placeholder="Optional contact number" /></Field><Field label="Address"><textarea name="address" value={form.address} onChange={change} rows="2" placeholder="Optional address" /></Field></>}
      <button className="button button-primary button-block" disabled={loading}>{loading ? 'Please wait…' : isRegister ? 'Create secure account' : 'Sign in'} <ArrowRight size={18} /></button>
    </form>
    {!isRegister && <div className="demo-access"><div className="divider"><span>Demo access</span></div><p>Select an identity, then press Sign in.</p><div className="demo-buttons">{demos.map(d => <button key={d.role} type="button" onClick={() => useDemo(d)}><b>{d.role}</b><span>{d.email}</span></button>)}</div></div>}
    <p className="auth-switch">{isRegister ? 'Already registered?' : 'New to CrimeWatch?'} <Link to={isRegister ? '/login' : '/register'}>{isRegister ? 'Sign in' : 'Create an account'}</Link></p></div></main>
  </div>
}

function Field({ icon: Icon, label, children }) { return <label className="field"><span>{Icon && <Icon size={15} />}{label}</span>{children}</label> }
