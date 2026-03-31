import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function TopNav() {
  const { user, token, logout } = useAuth();
  const navigate = useNavigate();

  const onLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header
      style={{
        position: 'sticky',
        top: 0,
        zIndex: 20,
        backdropFilter: 'blur(16px)',
        background: 'rgba(255,255,255,0.88)',
        borderBottom: '1px solid rgba(229,231,235,0.8)'
      }}
    >
      <div
        style={{
          maxWidth: 1024,
          margin: '0 auto',
          padding: '10px 16px',
          display: 'flex',
          alignItems: 'center',
          gap: 18
        }}
      >
        <Link
          to="/"
          style={{
            textDecoration: 'none',
            color: '#111827',
            fontWeight: 900,
            fontSize: 18,
            letterSpacing: 0.2
          }}
        >
          food<span style={{ color: '#fc8019' }}>.</span>ordering
        </Link>

        <Link to="/restaurants" style={{ textDecoration: 'none', color: '#4b5563', fontSize: 14 }}>
          Restaurants
        </Link>

        <Link to="/cart" style={{ textDecoration: 'none', color: '#4b5563', fontSize: 14 }}>
          Cart
        </Link>

        <Link to="/orders" style={{ textDecoration: 'none', color: '#4b5563', fontSize: 14 }}>
          My Orders
        </Link>

        <div style={{ marginLeft: 'auto', display: 'flex', alignItems: 'center', gap: 12 }}>
          {token && user ? (
            <>
              <span style={{ color: '#6b7280', fontSize: 13 }}>Hi, {user.name}</span>
              <button className="btn-ghost" onClick={onLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" style={{ textDecoration: 'none', color: '#4b5563', fontSize: 14 }}>
                Login
              </Link>
              <Link to="/register" style={{ textDecoration: 'none', color: '#4b5563', fontSize: 14 }}>
                Sign up
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}

