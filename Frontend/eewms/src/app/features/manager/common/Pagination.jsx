import React from "react";

const Pagination = ({ totalPages, size, currentPage, onPageChange, content }) => {
  const totalPageCount = Math.ceil(totalPages / size); // Số trang thực tế

  return (
    <nav className="flex flex-wrap items-center justify-between pt-4 flex-col md:flex-row" aria-label="Table navigation">
      <span className="block w-full mb-4 text-sm font-normal text-gray-500 dark:text-gray-400 md:mb-0 md:inline md:w-auto">
        Hiển thị{" "}
        <span className="font-semibold text-gray-900 dark:text-white">
          {1 + size * (currentPage - 1)}-{Math.min(size * currentPage, totalPages)}
        </span>{" "}
        trong{" "}
        <span className="font-semibold text-gray-900 dark:text-white">
          {totalPages}
        </span>{" "}
        {content}
      </span>
      <ul className="inline-flex h-8 -space-x-px text-sm rtl:space-x-reverse">
        <li>
          <a
            href="#"
            onClick={(e) => { e.preventDefault(); if (currentPage > 1) onPageChange(currentPage - 1); }}
            className="flex items-center justify-center h-8 px-3 leading-tight text-gray-500 bg-white border border-gray-300 rounded-s-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
          >
            Trước
          </a>
        </li>
        {Array.from({ length: totalPageCount }, (_, index) => index + 1).map((number) => (
          <li key={number}>
            <a
              href="#"
              onClick={(e) => { e.preventDefault(); onPageChange(number); }}
              className={`flex items-center justify-center px-3 h-8 leading-tight ${
                currentPage === number
                  ? "text-blue-600 border border-gray-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white"
                  : "text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
              }`}
            >
              {number}
            </a>
          </li>
        ))}
        <li>
          <a
            href="#"
            onClick={(e) => { e.preventDefault(); if (currentPage < totalPageCount) onPageChange(currentPage + 1); }}
            className="flex items-center justify-center h-8 px-3 leading-tight text-gray-500 bg-white border border-gray-300 rounded-e-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
          >
            Sau
          </a>
        </li>
      </ul>
    </nav>
  );
};

export default Pagination;