import { EditIcon, TrashIcon } from "lucide-react";
import Image from "next/image";

const TeacherRow = () => {
  return (
    <section key="1" className="container mx-auto px-4 py-12 md:px-6 lg:py-16">
      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        <div className="rounded-lg bg-white p-6 shadow-md dark:bg-gray-950 hover:shadow-lg transition-shadow duration-300 ease-in-out">
          <div className="flex items-center space-x-4">
            <Image
              alt="Teacher 1"
              className="h-16 w-16 rounded-full object-cover bg-slate-200"
              height={64}
              src="../placeholder.svg"
              style={{
                aspectRatio: "64/64",
                objectFit: "cover",
              }}
              width={64}
            />
            <div>
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">Nome</h3>
              <p className="text-gray-600 dark:text-gray-400">área de atuação</p>
            </div>
          </div>
          <ul className="mt-4 text-md leading-relaxed">
            <li className="flex flex-1 gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Matrícula:</h4>
              <p className="text-gray-600 dark:text-gray-400">123456</p>
            </li>
            <li className="flex flex-1 gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Campus:</h4>
              <p className="text-gray-600 dark:text-gray-400">Campus Exemplo</p>
            </li>
            <li className="flex flex-1 gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Lotação:</h4>
              <p className="text-gray-600 dark:text-gray-400">Lotação exemplo</p>
            </li>
            <li className="flex flex-1 gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Laboratório:</h4>
              <p className="text-gray-600 dark:text-gray-400">laboratorio exemplo</p>
            </li>
            <li className="flex flex-1 gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">E-mail:</h4>
              <p className="text-gray-600 dark:text-gray-400">exemplo2@gmail.com</p>
            </li>
          </ul>
          <div className="flex justify-end mt-4 space-x-2">
          <button
              className="px-2 py-2 bg-yellow-500 text-white hover:bg-yellow-600 transition-colors flex items-center rounded-full"
            >
              <EditIcon className="" />
            </button>
            <button
              className="px-2 py-2 bg-red-500 text-white rounded-full hover:bg-red-700 transition-colors flex items-center"
            >
              <TrashIcon className="" />
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default TeacherRow;
