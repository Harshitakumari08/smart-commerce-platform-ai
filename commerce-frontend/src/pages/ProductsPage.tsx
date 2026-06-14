import { useState } from 'react';
import { useProducts } from '../services/productService';
import { useCategories } from '../services/categoryService';
import ProductCard from '../components/sections/ProductCard';
import FilterPanel from '../components/sections/FilterPanel';
import SortDropdown from '../components/sections/SortDropdown';

export default function ProductsPage() {
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [minPrice, setMinPrice] = useState<number | undefined>(undefined);
  const [maxPrice, setMaxPrice] = useState<number | undefined>(undefined);
  const [sort, setSort] = useState('newest');
  const [page, setPage] = useState(0);

  // Fetch categories (always loaded for filter panel)
  const { data: categories = [], isLoading: loadingCategories } = useCategories();

  // Fetch products based on filter/pagination states
  const { data: paginatedData, isLoading: loadingProducts, isError } = useProducts({
    search,
    category,
    minPrice,
    maxPrice,
    sort,
    page,
    size: 9, // 9 items per page for a nice 3x3 grid
  });

  const handlePriceChange = (min: number | undefined, max: number | undefined) => {
    setMinPrice(min);
    setMaxPrice(max);
    setPage(0); // Reset to first page
  };

  const handleCategoryChange = (slug: string) => {
    setCategory(slug);
    setPage(0); // Reset to first page
  };

  const handleSearchChange = (term: string) => {
    setSearch(term);
    setPage(0); // Reset to first page
  };

  const handleSortChange = (option: string) => {
    setSort(option);
    setPage(0); // Reset to first page
  };

  const handleReset = () => {
    setSearch('');
    setCategory('');
    setMinPrice(undefined);
    setMaxPrice(undefined);
    setSort('newest');
    setPage(0);
  };

  const products = paginatedData?.content || [];
  const totalPages = paginatedData?.totalPages || 0;

  return (
    <main className="min-h-screen bg-[#030712] text-white py-12 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
      {/* Background ambient glow */}
      <div className="absolute top-10 left-10 w-72 h-72 bg-cyan-500/5 rounded-full blur-[100px] pointer-events-none"></div>
      <div className="absolute bottom-20 right-10 w-96 h-96 bg-indigo-500/5 rounded-full blur-[120px] pointer-events-none"></div>

      <div className="mx-auto max-w-7xl relative">
        <header className="mb-10">
          <h1 className="text-4xl font-extrabold tracking-tight bg-gradient-to-r from-white via-slate-100 to-slate-400 bg-clip-text text-transparent">
            Explore Products
          </h1>
          <p className="mt-2 text-sm text-slate-400">Discover premium products curated just for you</p>
        </header>

        <div className="grid gap-8 lg:grid-cols-4">
          {/* Left: Filter Sidebar */}
          <div className="lg:col-span-1">
            <FilterPanel
              categories={categories}
              selectedCategory={category}
              onSelectCategory={handleCategoryChange}
              minPrice={minPrice}
              maxPrice={maxPrice}
              onPriceChange={handlePriceChange}
              searchTerm={search}
              onSearchChange={handleSearchChange}
              onReset={handleReset}
            />
          </div>

          {/* Right: Products Area */}
          <div className="lg:col-span-3 space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 bg-slate-900/20 border border-slate-800/60 rounded-2xl p-4 backdrop-blur-sm">
              <p className="text-sm text-slate-400 font-medium">
                {loadingProducts ? (
                  <span>Loading products...</span>
                ) : (
                  <span>Showing <strong className="text-white">{paginatedData?.totalElements || 0}</strong> products</span>
                )}
              </p>
              <SortDropdown value={sort} onChange={handleSortChange} />
            </div>

            {/* Error state */}
            {isError && (
              <div className="rounded-2xl border border-red-500/20 bg-red-500/10 p-8 text-center text-red-400">
                Failed to load products. Please try reloading the page.
              </div>
            )}

            {/* Skeletons Loading state */}
            {loadingProducts ? (
              <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                {Array.from({ length: 6 }).map((_, i) => (
                  <div key={i} className="rounded-2xl border border-slate-800/80 bg-slate-900/20 p-4 animate-pulse space-y-4">
                    <div className="h-48 w-full bg-slate-800 rounded-xl"></div>
                    <div className="h-4 w-1/3 bg-slate-800 rounded"></div>
                    <div className="h-6 w-3/4 bg-slate-800 rounded"></div>
                    <div className="h-8 w-1/2 bg-slate-800 rounded"></div>
                  </div>
                ))}
              </div>
            ) : products.length === 0 ? (
              /* Empty state */
              <div className="rounded-2xl border border-slate-800/60 bg-slate-900/10 p-16 text-center backdrop-blur-sm">
                <svg className="mx-auto h-12 w-12 text-slate-600 stroke-current fill-none stroke-1.5" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5M10 11.25h4M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v-1.5c0 .621.504 1.125 1.125 1.125z" />
                </svg>
                <h3 className="mt-4 text-lg font-bold text-white">No products found</h3>
                <p className="mt-2 text-sm text-slate-400">Try adjusting your filters or resetting search to find what you are looking for.</p>
                <button
                  onClick={handleReset}
                  className="mt-6 rounded-xl bg-cyan-400 px-6 py-2.5 text-sm font-semibold text-slate-950 hover:bg-cyan-300 transition-colors"
                >
                  Reset Filters
                </button>
              </div>
            ) : (
              /* Product Grid */
              <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                {products.map((product) => (
                  <ProductCard key={product.id} product={product} />
                ))}
              </div>
            )}

            {/* Pagination Controls */}
            {totalPages > 1 && !loadingProducts && (
              <nav className="flex items-center justify-between border-t border-slate-800/80 pt-6">
                <button
                  onClick={() => setPage((p) => Math.max(0, p - 1))}
                  disabled={page === 0}
                  className="rounded-xl border border-slate-800 px-4 py-2 text-sm font-semibold text-slate-300 hover:bg-slate-800 hover:text-white transition-all disabled:opacity-30 disabled:pointer-events-none"
                >
                  Previous
                </button>
                <span className="text-sm font-medium text-slate-400">
                  Page <strong className="text-white">{page + 1}</strong> of <strong className="text-white">{totalPages}</strong>
                </span>
                <button
                  onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                  disabled={page === totalPages - 1}
                  className="rounded-xl border border-slate-800 px-4 py-2 text-sm font-semibold text-slate-300 hover:bg-slate-800 hover:text-white transition-all disabled:opacity-30 disabled:pointer-events-none"
                >
                  Next
                </button>
              </nav>
            )}
          </div>
        </div>
      </div>
    </main>
  );
}
