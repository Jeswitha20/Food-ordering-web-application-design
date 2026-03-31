import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../api/apiClient';
import { useCart } from '../cart/CartContext';

export default function CheckoutPage() {
  const navigate = useNavigate();
  const { cart, clearLocal } = useCart();

  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [instructions, setInstructions] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!cart || (cart.items?.length ?? 0) === 0) {
      setError('Your cart is empty.');
      return;
    }

    setSubmitting(true);
    try {
      await api.post('/api/v1/orders', { deliveryAddress, instructions });
      clearLocal();
      navigate('/orders');
    } catch (err: any) {
      setError(err?.response?.data?.message ?? 'Failed to place order');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <h2 className="page-title">Checkout</h2>
      {error && <div style={{ color: 'crimson', marginBottom: 10 }}>{error}</div>}

      <div className="card card--soft">
        <form onSubmit={onSubmit} style={{ display: 'grid', gap: 14, marginTop: 4 }}>
          <label style={{ display: 'grid', gap: 6, fontSize: 14 }}>
            <span>Delivery Address</span>
            <textarea
              value={deliveryAddress}
              onChange={(e) => setDeliveryAddress(e.target.value)}
              rows={3}
              required
            />
          </label>

          <label style={{ display: 'grid', gap: 6, fontSize: 14 }}>
            <span>Instructions (optional)</span>
            <textarea
              value={instructions}
              onChange={(e) => setInstructions(e.target.value)}
              rows={2}
            />
          </label>

          <button className="btn-primary" type="submit" disabled={submitting}>
            {submitting ? 'Placing...' : 'Place Order'}
          </button>
        </form>
      </div>
    </div>
  );
}

