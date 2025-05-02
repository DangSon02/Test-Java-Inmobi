import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { callApi, mainUrl } from "../util/api/requestUtils";

const PopupNapTien = ({ onClose }) => {
  const { t } = useTranslation();
  const [amount, setAmount] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      //   const res = await fetch("http://localhost:8080/api/nap-tien", {
      //     method: "POST",
      //     headers: { "Content-Type": "application/json" },
      //     body: JSON.stringify({
      //       userId: "user123", // sửa thành dữ liệu thật nếu cần
      //       amount: parseInt(amount),
      //     }),
      //   });

      const url = mainUrl + "/api/deposit";
      const res = await callApi(url, "POST", {
        amount: parseInt(amount),
      });
      if (res.status === "SUCCESS") {
        alert(`Nạp ${amount} VND thành công`);
        setAmount("");
        onClose();
      } else {
        alert("Nạp tiền thất bại");
      }
    } catch (err) {
      console.error(err);
      alert("Lỗi khi nạp tiền");
    }
  };

  return (
    <div className="main-popup ct-flex-col">
      <div style={styles.container}>
        {/* Nút đóng */}
        <div onClick={onClose} style={styles.closeButton}>
          X
        </div>
        {/* Tiêu đề */}
        <div style={styles.title}>{t("Nạp tiền")}</div>
        {/* Form nạp tiền */}
        <div style={styles.content}>
          <form onSubmit={handleSubmit}>
            <label style={styles.label}>{t("Nhập số tiền (VND):")}</label>
            <input
              type="number"
              min="1000"
              required
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              style={styles.input}
              placeholder="VD: 50000"
            />
            <button type="submit" style={styles.button}>
              {t("Xác nhận")}
            </button>
          </form>
        </div>
        {/* Background hình (nếu có)
        <img src={PopupHistory1} alt="" /> */}
      </div>
    </div>
  );
};

export default PopupNapTien;

// =========================
// 🎨 Style nội bộ
// =========================
const styles = {
  overlay: {
    position: "fixed", // cần fixed để nằm trên cùng
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0,0,0,0.6)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    zIndex: 9999, // lớn hơn mọi thứ khác
  },
  container: {
    background: "#fff",
    padding: 20,
    borderRadius: 8,
    width: 300,
    position: "relative",
    zIndex: 10000, // đảm bảo cao
  },
};
