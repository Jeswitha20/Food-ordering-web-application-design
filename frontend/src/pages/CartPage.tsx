import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../cart/CartContext';

export default function CartPage() {
  const navigate = useNavigate();
  const { cart, loading, refresh, updateItem, removeItem } = useCart();
  const [quantities, setQuantities] = useState<Record<number, number>>({});

  useEffect(() => {
    if (!cart || (cart.items?.length ?? 0) === 0) {
      void refresh();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (cart?.items) {
      const map: Record<number, number> = {};
      cart.items.forEach((it) => (map[it.menuItemId] = it.quantity));
      setQuantities(map);
    }
  }, [cart]);

  const total = cart?.totalAmount ?? 0;
  const items = useMemo(() => cart?.items ?? [], [cart]);

  return (
    <div>
      <h2 className="page-title">Your Cart</h2>

      {loading && <div>Loading cart...</div>}

      {!loading && items.length === 0 && (
        <div className="card card--soft">
          <div className="muted">Your cart is empty. Add items from restaurants.</div>
        </div>
      )}

      {items.length > 0 && (
        <div className="grid">
          {items.map((it) => (
            <div key={it.id} className="card card--soft">
              <div style={{ fontWeight: 900 }}>{it.name}</div>
              <div style={{ color: '#6b7280', marginTop: 6, fontSize: 13 }}>{it.description}</div>
              <div style={{ marginTop: 8 }}>
                <b>₹{it.price.toFixed(2)}</b> each
              </div>

              <div style={{ display: 'flex', gap: 10, alignItems: 'center', marginTop: 12 }}>
                <input
                  type="number"
                  min={1}
                  value={quantities[it.menuItemId] ?? it.quantity}
                  onChange={(e) =>
                    setQuantities((prev) => ({ ...prev, [it.menuItemId]: Number(e.target.value) }))
                  }
                  style={{ width: 90 }}
                />
                <button
                  className="btn-primary"
                  onClick={() =>
                    void updateItem(it.menuItemId, quantities[it.menuItemId] ?? it.quantity)
                  }
                >
                  Update
                </button>
                <button className="btn-ghost" onClick={() => void removeItem(it.menuItemId)}>
                  Remove
                </button>
                <div style={{ marginLeft: 'auto', fontWeight: 900 }}>Line: ₹{it.lineTotal.toFixed(2)}</div>
              </div>
            </div>
          ))}
        </div>
      )}

      {items.length > 0 && (
        <div style={{ marginTop: 18, display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ fontWeight: 900, fontSize: 18 }}>Total: ₹{total.toFixed(2)}</div>
          <button
            style={{ marginLeft: 'auto', padding: '10px 14px' }}
            onClick={() => navigate('/checkout')}
          >
            Checkout
          </button>
        </div>
      )}
    </div>
  );
}

