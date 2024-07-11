document.addEventListener("DOMContentLoaded", function() {
  const movieSection = document.getElementById("pelis");
  const movies = [];
  const guardarBtn = document.getElementById("guardarBtn");
  const actualizarBtn = document.getElementById("actualizarBtn");
  let isEditing = false;

  function loadMovieList() {
    console.log("Entro a loadMovieList");
    fetch("/pelis24115/movies?action=getAll")
      .then(response => response.json())
      .then(data => {
        data.forEach(movie => {
          movies.push(movie);
          const row = createMovieRow(movie); // Crear fila de película
          movieSection.appendChild(row); // Agregar fila a la tabla
        });
      })
      .catch(error => {
        console.error("Error al cargar las películas:", error);
      });
  }

  function createMovieRow(movie) {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${movie.nombre}</td>
      <td>${movie.descripcion}</td>
      <td>${movie.genero}</td>
      <td>${movie.anio}</td>
      <td>${movie.director}</td>
      <td style="color: gold;">${getStars(movie.calificacion)}</td>
      <td>
        <button type="button" class="btn btn-primary btn-sm editarBtn" data-id="${movie.id}">
          <i class="bi bi-pencil-square"></i> Editar
        </button>
        <button type="button" class="btn btn-danger btn-sm borrarBtn" data-id="${movie.id}">
          <i class="bi bi-trash"></i> Borrar
        </button>
      </td>
    `;

    const borrarBtn = row.querySelector('.borrarBtn');
    borrarBtn.addEventListener('click', () => {
      const movieId = borrarBtn.getAttribute('data-id');
      deleteMovie(movieId, row);
    });

    const editarBtn = row.querySelector('.editarBtn');
    editarBtn.addEventListener('click', () => {
      const movieId = editarBtn.getAttribute('data-id');
      editMovie(movieId);
    });

    return row;
  }

  function getStars(calificacion) {
    const maxStars = 5;
    const filledStars = Math.round(calificacion / 2);
    const emptyStars = maxStars - filledStars;
    return '★'.repeat(filledStars) + '☆'.repeat(emptyStars);
  }

  function addMovie(event) {
    event.preventDefault();

    const nombre = document.getElementById("titulo").value;
    const descripcion = document.getElementById("descripcion").value;
    const genero = document.getElementById("genero").value;
    const anio = document.getElementById("anio").value;
    const director = document.getElementById("director").value;
    const calificacion = document.getElementById("calificacion").value;

    if (!nombre || !descripcion || !genero || !anio || !director || !calificacion) {
      alert("Por favor, complete todos los campos.");
      return;
    }

    const movieData = {
      nombre,
      descripcion,
      genero,
      anio,
      director,
      calificacion
    };

    fetch("/pelis24115/movies", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: new URLSearchParams(movieData).toString()
    })
    .then(response => response.json())
    .then(data => {
      console.log(data.message);
      const row = createMovieRow(movieData);
      movieSection.appendChild(row);
      const modal = bootstrap.Modal.getInstance(document.getElementById('nuevoModal'));
      modal.hide();
      location.reload();
    })
    .catch(error => {
      console.error("Error al guardar la película:", error);
    });
  }

  function editMovie(movieId) {
    fetch(`/pelis24115/movies?action=getById&id=${movieId}`)
      .then(response => response.json())
      .then(data => {
        document.getElementById("titulo").value = data.nombre;
        document.getElementById("descripcion").value = data.descripcion;
        document.getElementById("genero").value = data.genero;
        document.getElementById("anio").value = data.anio;
        document.getElementById("director").value = data.director;
        document.getElementById("calificacion").value = data.calificacion;
  
        modalTitle.innerText = "Editar película";
        isEditing = true;
        guardarBtn.style.display = 'none';
        actualizarBtn.style.display = 'block';
  
        const modal = new bootstrap.Modal(document.getElementById('nuevoModal'));
        modal.show();
  
        document.getElementById("actualizarBtn").addEventListener("click", function updateMovie(event) {
          event.preventDefault();
  
          const nombre = document.getElementById("titulo").value;
          const descripcion = document.getElementById("descripcion").value;
          const genero = document.getElementById("genero").value;
          const anio = document.getElementById("anio").value;
          const director = document.getElementById("director").value;
          const calificacion = document.getElementById("calificacion").value;
  
          if (!nombre || !descripcion || !genero || !anio || !director || !calificacion) {
            alert("Por favor, complete todos los campos.");
            return;
          }
  
          const updatedMovie = {
            id: movieId,
            nombre: nombre,
            descripcion: descripcion,
            genero: genero,
            anio: anio,
            director: director,
            calificacion: calificacion
          };
  
          fetch(`/pelis24115/movies`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedMovie)
          })
          .then(response => response.json())
          .then(data => {
            console.log(data.message);
            modal.hide();
            location.reload();
          })
          .catch(error => {
            console.error("Error al actualizar la película:", error);
          });
  
          document.getElementById("actualizarBtn").removeEventListener("click", updateMovie);
        });
      })
      .catch(error => {
        console.error("Error al obtener la película:", error);
      });
  }
 

  function deleteMovie(movieId, row) {
    fetch(`/pelis24115/movies?id=${movieId}`, {
      method: "DELETE"
    })
    .then(response => {
      if (response.ok) {
        row.remove();
        console.log("Película eliminada correctamente.");
        location.reload();
      } else {
        console.error("Error al eliminar la película.");
      }
    })
    .catch(error => {
      console.error("Error al eliminar la película:", error);
    });
  }

  document.getElementById("guardarBtn").addEventListener("click", addMovie);
  document.getElementById("actualizarBtn").style.display = 'none'; 

  loadMovieList();
});
