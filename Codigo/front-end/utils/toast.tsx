export const showSuccessMessage = (message: string) => {
    const toaster = document.createElement("div");
    toaster.classList.add(
      "bg-green-500",
      "text-white",
      "p-4",
      "rounded",
      "fixed",
      "bottom-4",
      "left-1/2",
      "transform",
      "-translate-x-1/2",
    );
    toaster.textContent = message;
    document.body.appendChild(toaster);
    setTimeout(() => {
      document.body.removeChild(toaster);
    }, 3000);
  };