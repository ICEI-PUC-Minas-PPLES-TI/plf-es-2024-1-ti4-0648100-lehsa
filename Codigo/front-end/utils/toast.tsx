export const showSuccessMessage = (message: string) => {
  const toaster = document.createElement("div");
  toaster.classList.add(
    "bg-green-500",
    "text-white",
    "p-4",
    "rounded-md",
    "fixed",
    "bottom-4",
    "right-4",
    "transform",
    "-translate-x-1/2",
    "shadow-lg",
    "transition-all",
    "duration-300",
  );
  toaster.textContent = message;
  document.body.appendChild(toaster);
  setTimeout(() => {
    toaster.style.opacity = "0";
    setTimeout(() => {
      document.body.removeChild(toaster);
    }, 300);
  }, 4500);
};