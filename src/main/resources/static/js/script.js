function validateForm() {
  const crop = document.getElementById('crop').value.trim();
  const soil = document.getElementById('soil').value.trim();
  const season = document.getElementById('season').value.trim();

  if (!season || (crop === '' && soil === '')) {
    alert("Please enter crop + season OR soil + season OR all three");
    return false;
  }

  // after form submit (server will return result page), clear inputs locally
  // small delay so browser can submit
  setTimeout(() => {
    const f = document.getElementById('searchForm');
    if (f) f.reset();
  }, 300);

  return true;
}
