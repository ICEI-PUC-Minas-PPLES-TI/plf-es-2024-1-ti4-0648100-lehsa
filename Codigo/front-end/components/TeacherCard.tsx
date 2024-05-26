'use client'
import Cookie from 'js-cookie'
import { useEffect, useState } from "react";
import SingleTeacherCard from './SingleTeacherCard';
import { fetchTeachers } from '@/api/professor';


type Props = {
    id: string;
    nome: string;
    email: string;
    matricula: string;
    laboratorio: string;
    campus: string;
    lotacao: string;
    area_atuacao: string;
  }

const TeacherCard = () => {

    const [teacher, setTeacher] = useState<Props[]>([]);
    const token = Cookie.get("token");

    useEffect(() => {
        if (token) {
          fetchTeachers(token)
            .then(data => setTeacher(data))
            .catch(error => console.error('Error fetching items:', error));
        }
      }, [token]);

    const handleDelete = (id: string) => {
        setTeacher(teacher.filter(teacher => teacher.id !== id)); // Remover o professor da lista
      };

    return (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
            {teacher.map((teacher: Props) => (
                <div key={teacher.id}>
                    <SingleTeacherCard {...teacher}
                    onDelete={handleDelete}/>
                    </div>
            ))}
        </div>
    );
};

export default TeacherCard;