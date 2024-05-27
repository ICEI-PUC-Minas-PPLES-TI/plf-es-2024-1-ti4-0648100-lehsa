import React from "react";

interface UserTabsProps {
  activeTab: "Agendamentos" | "Emprestimos";
  setActiveTab: (tab: "Agendamentos" | "Emprestimos") => void;
}

const UserTabs: React.FC<UserTabsProps> = ({ activeTab, setActiveTab }) => {
  return (
    <div className="flex flex-row items-end relative">
      {activeTab === "Agendamentos" ? (
        <h2
          onClick={() => setActiveTab("Agendamentos")}
          className={`mb-[-0.75rem] px-5 pb-[1.25rem] pt-1 font-semibold text-lg text-center bg-white rounded-t-xl z-50 relative cursor-pointer`}
        >
          Agendamentos
          <div className="w-[1.5rem] h-[1.5rem] right-[-0.75rem] bottom-0 rounded-full bg-white z-10 absolute" />
          <div className="w-[1.5rem] h-[1.5rem] right-[-1.5rem] bottom-[0.75rem] rounded-full bg-[#f5f7fa] z-20 absolute" />
        </h2>
      ) : (
        <h2
          onClick={() => setActiveTab("Agendamentos")}
          className={`mb-0 px-5 pb-2 font-semibold text-lg text-center bg-[#f5f7fa] rounded-t-xl z-50 relative cursor-pointer`}
        >
          Agendamentos
        </h2>
      )}

      {activeTab === "Emprestimos" ? (
        <h2
          onClick={() => setActiveTab("Emprestimos")}
          className={`mb-0 ml-[0.5rem] px-5 pb-2 pt-1 font-semibold text-lg text-center bg-white rounded-t-xl z-50 relative cursor-pointer `}
        >
          Empréstimos
          <div className="w-[1.5rem] h-[1.5rem] left-[-1.5rem] bottom-0 rounded-full bg-[#f5f7fa] z-20 absolute" />
          <div className="w-[1.5rem] h-[1.5rem] left-[-0.75rem] bottom-[-0.75rem] rounded-full bg-white z-10 absolute" />
          <div className="w-[1.5rem] h-[1.5rem] right-[-0.75rem] bottom-[-0.75rem] rounded-full bg-white z-10 absolute" />
          <div className="w-[1.5rem] h-[1.5rem] right-[-1.5rem] bottom-0 rounded-full bg-[#f5f7fa] z-20 absolute" />
        </h2>
      ) : (
        <h2
          onClick={() => setActiveTab("Emprestimos")}
          className={`mb-0 ml-[0.5rem] px-5 pb-2 font-semibold text-lg text-center bg-[#f5f7fa] rounded-t-xl z-50 relative cursor-pointer `}
        >
          Empréstimos
        </h2>
      )}
    </div>
  );
};

export default UserTabs;
