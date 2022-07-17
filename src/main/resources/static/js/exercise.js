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

    let removeExerciseButton = document.createElement('button');
    removeExerciseButton.textContent = 'Remove exercise';
    removeExerciseButton.classList.add('fitcom-small-btn');
    removeExerciseButton.addEventListener('click', onRemoveExercise);

    let exerciseNumH5 = document.createElement('h5');
    exerciseNumH5.classList.add('exercise-num');
    exerciseNumH5.textContent = 'New Exercise';

    let currentExerciseDiv = document.createElement('div');
    currentExerciseDiv.appendChild(exerciseNumH5);
    currentExerciseDiv.appendChild(removeExerciseButton);
    currentExerciseDiv.appendChild(nameDiv);
    currentExerciseDiv.appendChild(descriptionDiv);
    currentExerciseDiv.appendChild(videoDiv);

    let exercisesSection = document.getElementById('exercises');

    exercisesSection.appendChild(currentExerciseDiv);
}

function onRemoveExercise(e) {
    e.preventDefault();
    e.target.parentElement.remove();
}

const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;

async function removeExercise(trainingId, exerciseName, element) {
    if (confirm('Are you sure you want to delete exercise ' + exerciseName + '?')) {
        await fetch(`http://localhost:8080/training-programs/${trainingId}/remove-exercise`, {
            method: 'DELETE',
            headers: {
                [csrfHeaderName]: csrfHeaderValue,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(exerciseName)
        }).then(() => {
            let buttonWrapper = element.parentElement;
            let headerElement = buttonWrapper.previousElementSibling;
            let nameDiv = buttonWrapper.nextElementSibling;
            let descriptionDiv = nameDiv.nextElementSibling;
            let videoDiv = descriptionDiv.nextElementSibling;

            buttonWrapper.remove();
            headerElement.remove();
            nameDiv.remove();
            descriptionDiv.remove();
            videoDiv.remove();
        })
    }
}