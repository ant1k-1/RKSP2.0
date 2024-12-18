import React, { useState, useEffect } from "react";
import axios from "axios";
import PingButton from "./PingButton";

const App = () => {
  const [files, setFiles] = useState([]);
  const [file, setFile] = useState(null);
  const [filename, setFilename] = useState("");

  // Получение списка файлов
  const fetchFiles = async () => {
    try {
      const response = await axios.get("/api/v1/file/");
      setFiles(response.data);
    } catch (error) {
      console.error("Ошибка при получении списка файлов:", error);
    }
  };

  // Загрузка файла
  const uploadFile = async (e) => {
    e.preventDefault();
    if (!file || !filename) {
      alert("Выберите файл и укажите имя!");
      return;
    }
    const formData = new FormData();
    formData.append("file", file);
    formData.append("filename", filename);

    try {
      await axios.post("/api/v1/file/upload", formData);
      setFile(null);
      setFilename("");
      fetchFiles(); // Обновляем список файлов
    } catch (error) {
      console.error("Ошибка при загрузке файла:", error);
    }
  };

  // Удаление файла
  const deleteFile = async (id) => {
    try {
      await axios.delete(`/api/v1/file/${id}/delete`);
      fetchFiles(); // Обновляем список файлов
    } catch (error) {
      console.error("Ошибка при удалении файла:", error);
    }
  };

  // Скачивание файла
  const downloadFile = async (id) => {
    try {
      const response = await axios.get(`/api/v1/file/${id}/download`, {
        responseType: "blob", // Важный параметр для скачивания
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "file"); // Укажите имя файла
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Ошибка при скачивании файла:", error);
    }
  };

  // Загружаем список файлов при монтировании
  useEffect(() => {
    fetchFiles();
  }, []);

  return (
    <div>
      <h1>Файлы</h1>
      <form onSubmit={uploadFile}>
        <input
          type="file"
          onChange={(e) => setFile(e.target.files[0])}
        />
        <input
          type="text"
          placeholder="Имя файла"
          value={filename}
          onChange={(e) => setFilename(e.target.value)}
        />
        <button type="submit">Загрузить</button>
      </form>

      <ul>
        {files.map((file) => (
          <li key={file.id}>
            {file.name}
            <button onClick={() => downloadFile(file.id)}>Скачать</button>
            <button onClick={() => deleteFile(file.id)}>Удалить</button>
          </li>
        ))}
      </ul>
      <PingButton/>
    </div>
  );
};

export default App;
