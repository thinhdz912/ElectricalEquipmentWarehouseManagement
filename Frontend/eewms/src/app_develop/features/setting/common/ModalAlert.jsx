import React from "react";
import ModalAlertConfirm from "./ModalAlertConfirm";

const ModalAlert = ({ handleClose, handleDelete, name }) => {
  return (
    <ModalAlertConfirm
      title="Xác nhận xóa"
      content={`Bạn có chắc chắn muốn xóa ${name} không?`}
      handleClose={handleClose}
      handleConfirm={handleDelete}
      titleBtnConfirm="Xóa"
    />
  );
};

export default ModalAlert;