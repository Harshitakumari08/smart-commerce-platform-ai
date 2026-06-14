import React, { useEffect, useState } from 'react';
import type { Category } from '../../services/categoryService';

interface FilterPanelProps {
  categories: Category[];
  selectedCategory: string;
  onSelectCategory: (category: string) => void;
  minPrice: number | undefined;
  maxPrice: number | undefined;
  onPriceChange: (min: number | undefined, max: number | undefined) => void;
  searchTerm: string;
  onSearchChange: (search: string) => void;
  onReset: () => void;
}

export default function FilterPanel({
  categories,
  selectedCategory,
  onSelectCategory,
  minPrice,
  maxPrice,
  onPriceChange,
  searchTerm,
  onSearchChange,
  onReset,
}: FilterPanelProps) {
  const [localSearch, setLocalSearch] = useState(searchTerm);
  const [localMin, setLocalMin] = useState(minPrice?.toString() || '');
  const [localMax, setLocalMax] = useState(maxPrice?.toString() || '');

  // Synchronize local search with prop changes
  useEffect(() => {
    setLocalSearch(searchTerm);
  }, [searchTerm]);

  // Handle debounced search input changes
  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      onSearchChange(localSearch);
    }, 400); // 400ms debounce delay
    return () => clearTimeout(delayDebounce);
  }, [localSearch]);

  const handleApplyPrice = (e: React.FormEvent) => {
    e.preventDefault();
    const min = localMin === '' ? undefined : Number(localMin);
    const max = localMax === '' ? undefined : Number(localMax);
    onPriceChange(min, max);
  };

  const handleResetFilters = () => {
    setLocalSearch('');
    setLocalMin('');
    setLocalMax('');
    onReset();
  };

  return (
    <aside className="w-full rounded-2xl border border-slate-800 bg-slate-900/40 backdrop-blur-md p-6 space-y-6">
      <div>
        <h2 className="text-lg font-bold text-white mb-4">Filters</h2>
        
        {/* Search Input */}
        <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
          Search Products
        </label>
        <div className="relative">
          <input
            type="text"
            className="w-full rounded-xl border border-slate-800 bg-slate-950/60 px-4 py-2.5 pl-10 text-white placeholder-slate-500 focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500 outline-none transition-all text-sm"
            placeholder="Search name, brand, SKU..."
            value={localSearch}
            onChange={(e) => setLocalSearch(e.target.value)}
          />
          <span className="absolute left-3.5 top-3 text-slate-500">
            <svg className="h-4.5 w-4.5 stroke-current fill-none stroke-2" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </span>
        </div>
      </div>

      <hr className="border-slate-800/80" />

      {/* Categories filter */}
      <div>
        <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-3">
          Categories
        </label>
        <div className="space-y-1.5 max-h-60 overflow-y-auto pr-1">
          <button
            onClick={() => onSelectCategory('')}
            className={`w-full text-left px-3 py-2 rounded-xl text-sm font-medium transition-all ${
              selectedCategory === ''
                ? 'bg-cyan-500/10 text-cyan-400 font-bold border-l-2 border-cyan-400 pl-4'
                : 'text-slate-400 hover:text-white hover:bg-slate-800/40'
            }`}
          >
            All Categories
          </button>
          {categories.map((cat) => (
            <button
              key={cat.id}
              onClick={() => onSelectCategory(cat.slug)}
              className={`w-full text-left px-3 py-2 rounded-xl text-sm font-medium transition-all ${
                selectedCategory === cat.slug
                  ? 'bg-cyan-500/10 text-cyan-400 font-bold border-l-2 border-cyan-400 pl-4'
                  : 'text-slate-400 hover:text-white hover:bg-slate-800/40'
              }`}
            >
              {cat.name}
            </button>
          ))}
        </div>
      </div>

      <hr className="border-slate-800/80" />

      {/* Price Range Filter */}
      <div>
        <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-3">
          Price Range
        </label>
        <form onSubmit={handleApplyPrice} className="space-y-3">
          <div className="flex gap-3 items-center">
            <input
              type="number"
              className="w-full rounded-xl border border-slate-800 bg-slate-950/60 px-3 py-2 text-white placeholder-slate-600 focus:border-cyan-500 outline-none text-sm"
              placeholder="Min ($)"
              value={localMin}
              onChange={(e) => setLocalMin(e.target.value)}
            />
            <span className="text-slate-600 text-sm">to</span>
            <input
              type="number"
              className="w-full rounded-xl border border-slate-800 bg-slate-950/60 px-3 py-2 text-white placeholder-slate-600 focus:border-cyan-500 outline-none text-sm"
              placeholder="Max ($)"
              value={localMax}
              onChange={(e) => setLocalMax(e.target.value)}
            />
          </div>
          <button
            type="submit"
            className="w-full rounded-xl bg-slate-800 text-sm text-slate-300 py-2 hover:bg-slate-700 active:scale-[0.98] transition-all font-semibold"
          >
            Apply Price
          </button>
        </form>
      </div>

      <hr className="border-slate-800/80" />

      {/* Reset button */}
      <button
        onClick={handleResetFilters}
        className="w-full rounded-xl border border-slate-800 text-sm text-slate-400 py-2.5 hover:bg-slate-800/50 hover:text-white transition-all font-semibold"
      >
        Clear All Filters
      </button>
    </aside>
  );
}
