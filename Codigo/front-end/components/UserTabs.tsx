import React from "react";

interface UserTabsProps {
  activeTab: "Agendamentos" | "Emprestimos";
  setActiveTab: (tab: "Agendamentos" | "Emprestimos") => void;
}

const UserTabs: React.FC<UserTabsProps> = ({ activeTab, setActiveTab }) => {
  return (
    <div className="flex flex-row items-end relative">
      <h2
        className={`mb-0 ml-5 px-5 pb-2 font-semibold text-xl text-center rounded-t-xl z-50 relative cursor-pointer ${
          activeTab === "Agendamentos" ? "bg-white" : "bg-[#f5f7fa]"
        }`}
        onClick={() => setActiveTab("Agendamentos")}
      >
        Agendamentos
        <div className="w-[1.5rem] h-[1.5rem] right-[-0.75rem] bottom-[-0.75rem] rounded-full bg-white z-10 absolute" />
        <div className="w-[1.5rem] h-[1.5rem] right-[-1.5rem] bottom-0 rounded-full bg-[#f5f7fa] z-20 absolute" />
      </h2>
      <h2
        className={`mb-0 ml-[0.5rem] px-5 pb-2 font-semibold text-xl text-center rounded-t-xl z-50 relative cursor-pointer ${
          activeTab === "Emprestimos" ? "bg-white" : "bg-[#f5f7fa]"
        }`}
        onClick={() => setActiveTab("Emprestimos")}
      >
        Emprestimos
        <div className="w-[1.5rem] h-[1.5rem] left-[-1.5rem] bottom-0 rounded-full bg-[#f5f7fa] z-20 absolute" />
        <div className="w-[1.5rem] h-[1.5rem] left-[-0.75rem] bottom-[-0.75rem] rounded-full bg-white z-10 absolute" />
        <div className="w-[1.5rem] h-[1.5rem] right-[-0.75rem] bottom-[-0.75rem] rounded-full bg-white z-10 absolute" />
        <div className="w-[1.5rem] h-[1.5rem] right-[-1.5rem] bottom-0 rounded-full bg-[#f5f7fa] z-20 absolute" />
      </h2>
    </div>
  );
};

export default UserTabs;
