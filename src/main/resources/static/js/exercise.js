// document.getElementById('add-exercise')
//     .addEventListener('click', addExercise);

let exercisesNum = 0;

function addExercise() {
    let nameDiv = document.createElement('div');
    nameDiv.classList.add('training-label-wrapper');

    let nameLabel = document.createElement('label');
    nameLabel.textContent = "Name:";
    nameLabel.htmlFor = 'exName';

    let nameInput = document.createElement('input');
    nameInput.type = "text";
    nameInput.id = "exName";
    nameInput.name = "exercisesData";

    nameDiv.appendChild(nameLabel);
    nameDiv.appendChild(nameInput);

    let descriptionDiv = document.createElement('div');
    descriptionDiv.classList.add('training-label-wrapper');

    let descriptionLabel = document.createElement('label');
    descriptionLabel.textContent = "Description:";
    descriptionLabel.htmlFor = 'exDescription';

    let descriptionTextarea = document.createElement('textarea');
    descriptionTextarea.id = "exDescription";
    descriptionTextarea.cols = 45;
    descriptionTextarea.rows = 5;
    descriptionTextarea.name = "exercisesData";

    descriptionDiv.appendChild(descriptionLabel);
    descriptionDiv.appendChild(descriptionTextarea);

    let videoDiv = document.createElement('div');
    videoDiv.classList.add('training-label-wrapper');

    let videoLabel = document.createElement('label');
    videoLabel.textContent = "Video:";
    videoLabel.htmlFor = 'exVideo';

    let videoInput = document.createElement('input');
    videoInput.type = "text";
    videoInput.id = "exVideo";
    videoInput.name = "exercisesData";

    videoDiv.appendChild(videoLabel);
    videoDiv.appendChild(videoInput);

    let exerciseNumH5 = document.createElement('h5');
    exerciseNumH5.classList.add('exercise-num');
    exerciseNumH5.textContent = `New Exercise ${++exercisesNum}`;

    let exercisesSection = document.getElementById('exercises');

    exercisesSection.appendChild(exerciseNumH5);
    exercisesSection.appendChild(nameDiv);
    exercisesSection.appendChild(descriptionDiv);
    exercisesSection.appendChild(videoDiv);
}