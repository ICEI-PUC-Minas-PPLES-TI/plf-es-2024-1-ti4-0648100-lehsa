import Cookie from "js-cookie";

// Função para buscar os dados de um professor pelo ID
export const getTeacherData = async (id: string) => {
  const token = Cookie.get("token");
  const response = await fetch(`http://localhost:8080/professor/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (response.ok) {
    return await response.json();
  } else {
    throw new Error("Falha ao buscar os dados do professor");
  }
};

// Função para atualizar os dados de um professor
export const updateTeacherData = async (id: string, formData: FormData) => {
  const token = Cookie.get("token");
  const response = await fetch(`http://localhost:8080/professor/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  });

  if (!response.ok) {
    throw new Error("Falha ao atualizar os dados do professor");
  }
};

// Função para deletar um professor
export const deleteTeacherData = async (id: string | string[]) => {
    try {
        const token = Cookie.get("token");
        if (!token) {
          throw new Error("Usuário não autenticado");
        }
    
        const response = await fetch(`http://localhost:8080/professor/${id}`, {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
    
        if (!response.ok) {
          throw new Error("Failed to delete item");
        }
    
        return true;
      } catch (error) {
        console.error("Failed to delete item:", error);
        return false;
      }
  };

  export const fetchTeachers = async (token: string) => {
    const response = await fetch("http://localhost:8080/professor", {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        "Authorization": `Bearer ${token}`
      }
    });
  
    if (!response.ok) {
      throw new Error('Failed to fetch teachers');
    }
  
    return response.json();
  };