import { beforeEach, describe, expect, it } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import App from '../App'
import Landing from '../pages/Landing'

beforeEach(() => localStorage.clear())

describe('CrimeWatch public experience', () => {
  it('renders the professional landing page with primary reporting action', () => {
    render(<MemoryRouter><Landing /></MemoryRouter>)
    expect(screen.getByRole('heading', { name: /every report deserves/i })).toBeInTheDocument()
    expect(screen.getAllByRole('link', { name: /report an incident/i }).length).toBeGreaterThan(0)
  })

  it('offers all three synthetic demo identities on login', () => {
    render(<MemoryRouter initialEntries={['/login']}><App /></MemoryRouter>)
    expect(screen.getByRole('heading', { name: /sign in to crimewatch/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /citizen/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /officer/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /admin/i })).toBeInTheDocument()
  })

  it('fills demo credentials when a role is selected', () => {
    render(<MemoryRouter initialEntries={['/login']}><App /></MemoryRouter>)
    fireEvent.click(screen.getByRole('button', { name: /admin/i }))
    expect(screen.getByPlaceholderText('name@example.com')).toHaveValue('admin@crimewatch.demo')
    expect(screen.getByPlaceholderText('Minimum 8 characters')).toHaveValue('Admin@123')
  })
})

describe('route protection', () => {
  it('redirects unauthenticated workspace access to login', () => {
    render(<MemoryRouter initialEntries={['/app']}><App /></MemoryRouter>)
    expect(screen.getByRole('heading', { name: /sign in to crimewatch/i })).toBeInTheDocument()
  })
})
