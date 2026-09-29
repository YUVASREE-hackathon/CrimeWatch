const API_URL = import.meta.env.VITE_API_URL || '/api'

export async function api(path, options = {}) {
  const token = localStorage.getItem('crimewatch_token')
  const headers = { ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }), ...options.headers }
  if (token) headers.Authorization = `Bearer ${token}`
  const response = await fetch(`${API_URL}${path}`, { ...options, headers })
  if (response.status === 204) return null
  const text = await response.text()
  const data = text ? JSON.parse(text) : null
  if (!response.ok) {
    const message = data?.message || data?.error || 'Something went wrong. Please try again.'
    const error = new Error(message)
    error.status = response.status
    error.fields = data?.fields || {}
    throw error
  }
  return data
}

export const formatStatus = value => (value || '').replaceAll('_', ' ').replace(/\b\w/g, c => c.toUpperCase())
export const formatDate = value => value ? new Intl.DateTimeFormat('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }).format(new Date(value)) : '—'

