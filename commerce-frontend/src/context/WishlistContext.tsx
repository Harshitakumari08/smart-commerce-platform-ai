import React, { createContext, useContext, useMemo, useState } from 'react';
import type { WishlistItem } from '../types';

interface WishlistContextValue {
  items: WishlistItem[];
  toggleItem: (item: WishlistItem) => void;
}

const WishlistContext = createContext<WishlistContextValue | undefined>(undefined);

export function WishlistProvider({ children }: { children: React.ReactNode }) {
  const [items, setItems] = useState<WishlistItem[]>([]);

  const value = useMemo<WishlistContextValue>(
    () => ({
      items,
      toggleItem: (item) => setItems((prev) => (prev.some((entry) => entry.id === item.id) ? prev.filter((entry) => entry.id !== item.id) : [...prev, item])),
    }),
    [items]
  );

  return <WishlistContext.Provider value={value}>{children}</WishlistContext.Provider>;
}

export function useWishlist() {
  const ctx = useContext(WishlistContext);
  if (!ctx) throw new Error('useWishlist must be used within WishlistProvider');
  return ctx;
}
