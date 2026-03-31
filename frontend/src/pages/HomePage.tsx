import React from 'react';
import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <div className="home-hero">
      <div className="home-hero__inner">
        <h1 className="hero-heading">Order food from your favorite restaurants</h1>
        <p className="hero-subtext">
          Browse menus, build your cart, place orders, and get instant email confirmations.
        </p>
        <div className="hero-actions">
          <Link to="/restaurants" style={{ textDecoration: 'none' }}>
            <button className="btn-primary hero-btn" type="button">
              Browse Restaurants
            </button>
          </Link>
          <Link to="/orders" style={{ textDecoration: 'none' }}>
            <button className="btn-primary hero-btn" type="button">
              My Orders
            </button>
          </Link>
          <Link to="/cart" style={{ textDecoration: 'none' }}>
            <button className="btn-primary hero-btn" type="button">
              Go to Cart
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
}

