const clearPage = () => {
    const main = document.querySelector('main');
    main.innerHTML = '';
  };
  
  const renderPageTitle = (title) => {
    if (!title) return;
    const main = document.querySelector('main');
    const pageTitle = document.createElement('h4');
    pageTitle.innerText = title;
    main.appendChild(pageTitle);
  };
  
  const hideFooter = () => {
    const footer = document.querySelector('footer');
    footer.style.display = 'none';
  };
  
  
  export { clearPage, renderPageTitle, hideFooter};
  