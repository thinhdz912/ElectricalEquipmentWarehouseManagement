import { useEffect, useRef, useState } from "react";
import { FaPlus } from "react-icons/fa";
import { toast } from "react-toastify";
import ModalAlert from "./common/ModalAlert";
import ModalConfirmCreate from "./common/ModalConfirmCreate";
import Pagination from "./common/Pagination";
import { axiosInstance } from "../../utils/axiosInstant";

const BranchManagement = () => {
  const [isOpenModalCreate, setIsOpenModalCreate] = useState(false);
  const [brandName, setBrandName] = useState("");
  const [statusModal, setStatusModal] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [listBranch, setListBranch] = useState(null);
  const [currentBranch, setCurrentBranch] = useState(null);
  const [isOpenDelete, setIsOpenDelete] = useState(false);
  const [isConfirming, setIsConfirming] = useState(false); // State cho modal xác nhận
  const [currentPage, setCurrentPage] = useState(1);
  const size = 6;
  const inputRef = useRef(null);

  const getListBranch = () => {
    setIsLoading(true);
    axiosInstance
      .get("/brand/find-all", { params: { size, page: currentPage - 1 } })
      .then((res) => setListBranch(res.data))
      .catch((err) => {
        if (err.response) toast.error(err.response.data.message);
        else if (err.request) toast.error("Network error occurred");
        else toast.error(err.message);
      })
      .finally(() => setIsLoading(false));
  };

  const handleOpenModalCreate = () => {
    setIsOpenModalCreate(true);
    setStatusModal(false);
    setTimeout(() => inputRef.current?.focus(), 0);
  };

  const handleOpenModalEdit = (branch) => {
    setCurrentBranch(branch);
    setBrandName(branch?.name || "");
    setIsOpenModalCreate(true);
    setStatusModal(true);
  };

  const handleCloseModalCreate = () => {
    setIsOpenModalCreate(false);
    setBrandName("");
    setCurrentBranch(null);
  };

  const handleOpenModalDelete = (branch) => {
    setCurrentBranch(branch);
    setIsOpenDelete(true);
  };

  const handleCloseModalDelete = () => {
    setIsOpenDelete(false);
    setCurrentBranch(null);
  };

  const handleDeleteBranch = () => {
    axiosInstance
      .delete(`/brand/delete/${currentBranch?.code}`)
      .then((res) => {
        toast.success(`Xoá thương hiệu ${currentBranch?.name} thành công!`);
        handleCloseModalDelete();
        getListBranch();
      })
      .catch((err) => {
        if (err.response) toast.error(err.response.data.message);
        else if (err.request) toast.error("Network error occurred");
        else toast.error(err.message);
      });
  };

  const handleSubmitForm = () => {
    if (!brandName?.trim()) {
      toast.warn("Vui lòng điền đầy đủ thông tin!");
      return;
    }
    setIsConfirming(true); // Mở modal xác nhận
  };

  const handleConfirmSubmit = () => {
    setIsLoading(true);
    setIsConfirming(false);
    const data = { brandName };
    const request = statusModal
      ? axiosInstance.put(`/brand/update/${currentBranch?.code}`, data)
      : axiosInstance.post("/brand/create", data);

    request
      .then((res) => {
        toast.success(`Thành công! ${statusModal ? "Sửa" : "Thêm"} thương hiệu ${brandName}`);
        handleCloseModalCreate();
        getListBranch();
      })
      .catch((err) => {
        if (err.response) toast.error(err.response.data.message);
        else if (err.request) toast.error("Network error occurred");
        else toast.error(err.message);
      })
      .finally(() => setIsLoading(false));
  };

  useEffect(() => getListBranch(), [currentPage]);
  useEffect(() => setBrandName(currentBranch?.name || ""), [currentBranch]);

  useEffect(() => {
    const handleKeyDown = (event) => {
      if (event.key === "Enter" && isOpenModalCreate && !isConfirming) {
        event.preventDefault();
        handleSubmitForm();
      }
    };
    document.addEventListener("keydown", handleKeyDown);
    return () => document.removeEventListener("keydown", handleKeyDown);
  }, [isOpenModalCreate, isConfirming]);

  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left text-gray-500 rtl:text-right dark:text-gray-400">
        <caption className="p-5 text-lg font-semibold text-left text-gray-900 bg-white rtl:text-right dark:text-white dark:bg-gray-800">
          <div className="flex justify-between">
            <div className="flex flex-col">
              Danh sách thương hiệu
              <p className="mt-1 text-sm font-normal text-gray-500 dark:text-gray-400">
                Thông tin danh sách thương hiệu
              </p>
            </div>
            <button
              className="flex items-center gap-2 px-4 py-2 text-base text-white bg-blue-500 rounded-md outline-none"
              onClick={handleOpenModalCreate}
            >
              <FaPlus />
              Thêm thương hiệu
            </button>
          </div>
        </caption>
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">
              Tên thương hiệu
            </th>
            <th scope="col" className="px-6 py-3">
              Hành động
            </th>
            <th scope="col" className="px-6 py-3"></th>
          </tr>
        </thead>
        <tbody>
          {listBranch?.content?.map((item, index) => (
            <tr
              key={item?.code || index}
              className="bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
            >
              <th
                scope="row"
                className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
              >
                {item?.name || "N/A"}
              </th>
              <td className="px-6 py-4">
                <a
                  onClick={() => handleOpenModalEdit(item)}
                  className="font-medium text-blue-600 cursor-pointer dark:text-blue-500 hover:underline"
                >
                  Chỉnh sửa
                </a>
              </td>
              <td className="px-6 py-4">
                <a
                  onClick={() => handleOpenModalDelete(item)}
                  className="font-medium text-red-600 cursor-pointer dark:text-red-500 hover:underline"
                >
                  Xóa
                </a>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="p-3">
        <Pagination
          totalPages={listBranch?.totalPages || 1}
          size={size}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          content="thương hiệu"
        />
      </div>
      {isOpenModalCreate && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-800 bg-opacity-50 animate-fadeIn">
          <div className="relative z-50 w-full max-w-2xl max-h-screen overflow-y-auto bg-white rounded-lg shadow-lg">
            <div className="flex items-center justify-between p-4 border-b border-gray-200 rounded-t md:p-5 dark:border-gray-600">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                {statusModal ? "Chỉnh sửa thương hiệu" : "Thêm thương hiệu mới"}
              </h3>
              <button
                type="button"
                onClick={handleCloseModalCreate}
                className="inline-flex items-center justify-center w-8 h-8 text-sm text-gray-400 bg-transparent rounded-lg hover:bg-gray-200 hover:text-gray-900 ms-auto dark:hover:bg-gray-600 dark:hover:text-white"
              >
                <svg
                  className="w-3 h-3"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 14 14"
                >
                  <path
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
                  />
                </svg>
                <span className="sr-only">Close modal</span>
              </button>
            </div>
            <div className="p-4 md:p-5">
              <div className="grid grid-cols-2 gap-4 mb-4">
                <div className="col-span-2">
                  <label
                    htmlFor="fullName"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Tên thương hiệu
                  </label>
                  <input
                    ref={inputRef}
                    type="text"
                    name="fullName"
                    id="fullName"
                    value={brandName}
                    onChange={(e) => setBrandName(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Nhập tên danh mục"
                    required
                  />
                </div>
              </div>
              <button
                onClick={handleSubmitForm}
                className="text-white inline-flex items-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 mt-6"
                disabled={isLoading}
              >
                {statusModal ? "Lưu" : "Thêm danh mục"}
              </button>
            </div>
          </div>
        </div>
      )}

      {isOpenDelete && (
        <ModalAlert
          handleClose={handleCloseModalDelete}
          handleDelete={handleDeleteBranch}
          name={currentBranch?.name}
        />
      )}

      {isConfirming && (
        <ModalConfirmCreate
          handleClose={() => setIsConfirming(false)}
          handleConfirm={handleConfirmSubmit}
          type={statusModal ? "sửa" : "thêm"}
        />
      )}

      <style jsx>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
          }
          to {
            opacity: 1;
          }
        }

        @keyframes slideIn {
          from {
            transform: translateY(-20%);
          }
          to {
            transform: translateY(0);
          }
        }

        .animate-fadeIn {
          animation: fadeIn 0.3s ease-in-out;
        }

        .animate-slideIn {
          animation: slideIn 0.3s ease-in-out;
        }
      `}</style>
    </div>
  );
};

export default BranchManagement;
