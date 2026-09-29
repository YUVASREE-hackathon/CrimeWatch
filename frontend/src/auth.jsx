import { createContext, useContext, useMemo, useState } from 'react'
import { api } from './api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('crimewatch_user')) } catch { return null }
  })
  const save = response => {
    localStorage.setItem('crimewatch_token', response.token)
    localStorage.setItem('crimewatch_user', JSON.stringify(response.user))
    setUser(response.user)
  }
  const login = async values => { const response = await api('/auth/login', { method: 'POST', body: JSON.stringify(values) }); save(response); return response }
  const register = async values => { const response = await api('/auth/register', { method: 'POST', body: JSON.stringify(values) }); save(response); return response }
  const logout = () => { localStorage.removeItem('crimewatch_token'); localStorage.removeItem('crimewatch_user'); setUser(null) }
  const value = useMemo(() => ({ user, login, register, logout, setUser }), [user])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = () => useContext(AuthContext)
