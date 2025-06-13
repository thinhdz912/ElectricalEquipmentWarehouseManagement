import React from "react";
import ModalAlertConfirm from "./ModalAlertConfirm";

const ModalConfirmCreate = ({ handleClose, handleConfirm, type }) => {
  return (
    <ModalAlertConfirm
      title={`Xác nhận ${type}`}
      content="Bạn đã kiểm tra thông tin kĩ càng?"
      handleClose={handleClose}
      handleConfirm={handleConfirm}
      titleBtnConfirm="Xác nhận"
    />
  );
};

export default ModalConfirmCreate;