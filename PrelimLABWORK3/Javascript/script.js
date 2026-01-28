// Constants
const MAX_ATTENDANCE = 5;
const MAX_ABSENCES_ALLOWED = 3;

// Input validation - only allow numbers and decimal point
const inputs = document.querySelectorAll('input[type="text"]');
inputs.forEach(input => {
    input.addEventListener('input', function() {
        // Remove non-numeric characters except decimal point
        this.value = this.value.replace(/[^\d.]/g, '');
        
        // Only allow one decimal point
        const parts = this.value.split('.');
        if (parts.length > 2) {
            this.value = parts[0] + '.' + parts.slice(1).join('');
        }
        
        // Parse the value
        const value = parseFloat(this.value);
        
        // Check limits based on field type
        if (this.id === 'attendance') {
            // For attendance field - max is 5
            if (!isNaN(value) && value > MAX_ATTENDANCE) {
                this.value = MAX_ATTENDANCE.toString();
            }
            // Prevent multiple leading zeros like "00000"
            if (this.value.length > 1 && this.value[0] === '0' && this.value[1] !== '.') {
                this.value = this.value.substring(1);
            }
        } else {
            // For lab grade fields - max is 100
            if (!isNaN(value) && value > 100) {
                this.value = '100';
            }
            // Prevent multiple leading zeros like "00000"
            if (this.value.length > 1 && this.value[0] === '0' && this.value[1] !== '.') {
                this.value = this.value.substring(1);
            }
            // Prevent values like "111" when max is 100
            if (!isNaN(value) && value > 100) {
                this.value = '100';
            }
        }
        
        // Limit decimal places to 2
        if (this.value.includes('.')) {
            const decimalParts = this.value.split('.');
            if (decimalParts[1] && decimalParts[1].length > 2) {
                this.value = decimalParts[0] + '.' + decimalParts[1].substring(0, 2);
            }
        }
    });
    
    input.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') calculate();
    });
});

function showNotification(message) {
    alert(message);
}

function showAttendanceModal(attendance, absences, excusedAbsences, unexcusedAbsences) {
    const modal = document.getElementById('attendanceModal');
    const modalBody = document.getElementById('modalBody');
    
    modalBody.innerHTML = `
        <p><strong>⚠️ AUTOMATIC FAILURE</strong></p>
        <p style="margin-top: 12px;">You have <strong>${unexcusedAbsences} unexcused absence(s)</strong></p>
        <p style="margin-top: 12px;">You have <strong>FAILED</strong> due to excessive unexcused absences!</p>
        <p style="margin-top: 8px;">Total absences: <strong>${absences}</strong></p>
        <p style="margin-top: 8px;">Excused absences: <strong>${excusedAbsences}</strong></p>
        <p style="margin-top: 8px;">Unexcused absences: <strong>${unexcusedAbsences}</strong></p>
        <p style="margin-top: 8px;">Maximum allowed unexcused: <strong>${MAX_ABSENCES_ALLOWED}</strong></p>
        <p style="margin-top: 16px; font-weight: 600;">You cannot proceed with grade calculation.</p>
        <p style="margin-top: 8px;">Please attend more classes to meet the requirement.</p>
    `;
    
    modal.classList.add('show');
}

function showExcuseModal(absences, callback) {
    const modal = document.getElementById('excuseModal');
    const input = document.getElementById('excusedInput');
    const maxLabel = document.getElementById('maxExcusedLabel');
    
    // Show max allowed in label
    maxLabel.textContent = `(Max: ${absences})`;
    maxLabel.style.color = 'var(--gray-600)';
    maxLabel.style.fontSize = '0.9rem';
    
    // Reset and set up the input field
    input.value = '0';
    input.maxLength = absences.toString().length + 1;
    
    // Add real-time validation to the excuse input
    input.oninput = function() {
        // Remove non-numeric characters
        this.value = this.value.replace(/[^\d]/g, '');
        
        // Prevent leading zeros
        if (this.value.length > 1 && this.value[0] === '0') {
            this.value = this.value.substring(1);
        }
        
        // Limit to max absences
        const value = parseInt(this.value) || 0;
        if (value > absences) {
            this.value = absences.toString();
        }
    };
    
    modal.classList.add('show');
    
    // Store the callback for later use
    window.excuseCallback = callback;
    window.maxExcused = absences;
}

function closeModal() {
    const attendanceModal = document.getElementById('attendanceModal');
    const excuseModal = document.getElementById('excuseModal');
    attendanceModal.classList.remove('show');
    excuseModal.classList.remove('show');
}

function handleExcuseNo() {
    closeModal();
    if (window.excuseCallback) {
        window.excuseCallback(0);
        window.excuseCallback = null;
    }
}

function handleExcuseYes() {
    const input = document.getElementById('excusedInput');
    const excused = parseInt(input.value) || 0;
    
    if (excused < 0 || excused > window.maxExcused) {
        showNotification(`Excused absences must be between 0 and ${window.maxExcused}!`);
        return;
    }
    
    closeModal();
    if (window.excuseCallback) {
        window.excuseCallback(excused);
        window.excuseCallback = null;
    }
}

// Close modal when clicking outside of it
window.onclick = function(event) {
    const attendanceModal = document.getElementById('attendanceModal');
    const excuseModal = document.getElementById('excuseModal');
    if (event.target === attendanceModal) {
        closeModal();
    }
    if (event.target === excuseModal) {
        closeModal();
    }
}

function calculate() {
    const attendance = parseFloat(document.getElementById('attendance').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);

    if (isNaN(attendance) || isNaN(lab1) || isNaN(lab2) || isNaN(lab3)) {
        showNotification('Please enter valid numbers in all fields!');
        return;
    }
    if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
        showNotification('Lab grades must be between 0 and 100!');
        return;
    }
    if (attendance < 0 || attendance > MAX_ATTENDANCE) {
        showNotification(`Attendance must be between 0 and ${MAX_ATTENDANCE}!`);
        return;
    }

    // Calculate absences
    const absences = MAX_ATTENDANCE - Math.floor(attendance);

    // If there are absences, ask about excused absences
    if (absences > 0) {
        showExcuseModal(absences, function(excusedAbsences) {
            processCalculation(attendance, lab1, lab2, lab3, absences, excusedAbsences);
        });
    } else {
        processCalculation(attendance, lab1, lab2, lab3, absences, 0);
    }
}

function processCalculation(attendance, lab1, lab2, lab3, absences, excusedAbsences) {
    const unexcusedAbsences = absences - excusedAbsences;

    // Check if unexcused absences exceed the allowed limit - AUTOMATIC FAILURE
    if (unexcusedAbsences > MAX_ABSENCES_ALLOWED) {
        showAttendanceModal(attendance, absences, excusedAbsences, unexcusedAbsences);
        return; // Stop here - don't calculate or show results
    }

    // Add excused absences to attendance for calculation (each excused = +1 attendance)
    const effectiveAttendance = attendance + excusedAbsences;
    
    // Convert effective attendance to percentage (0-100 scale)
    const attendancePercentage = (effectiveAttendance / MAX_ATTENDANCE) * 100;

    const labWorkAverage = (lab1 + lab2 + lab3) / 3;
    const classStanding = (attendancePercentage * 0.4) + (labWorkAverage * 0.6);

    const requiredPrelimForPassing = (75 - (classStanding * 0.7)) / 0.3;
    const requiredPrelimForExcellent = (100 - (classStanding * 0.7)) / 0.3;

    displayResults(attendance, absences, excusedAbsences, unexcusedAbsences, effectiveAttendance, 
                   lab1, lab2, lab3, labWorkAverage, classStanding, 
                   requiredPrelimForPassing, requiredPrelimForExcellent, attendancePercentage);
}

function displayResults(attendance, absences, excusedAbsences, unexcusedAbsences, effectiveAttendance,
                        lab1, lab2, lab3, labWorkAverage, classStanding, 
                        requiredPrelimForPassing, requiredPrelimForExcellent, attendancePercentage) {
    const resultsCard = document.getElementById('resultsCard');
    const resultsContent = document.getElementById('resultsContent');
    let html = '';

    html += `<div class="stat-section">
        <h3>📊 Computation Results</h3>
        <div class="stat-row"><span>Actual Attendance:</span><span>${Math.floor(attendance)}/${MAX_ATTENDANCE}</span></div>
        <div class="stat-row"><span>Total Absences:</span><span>${absences}</span></div>
        <div class="stat-row"><span>Excused Absences:</span><span>${excusedAbsences}</span></div>
        <div class="stat-row"><span>Unexcused Absences:</span><span>${unexcusedAbsences}/${MAX_ABSENCES_ALLOWED} allowed ✓</span></div>
        <div class="stat-row"><span>Effective Attendance:</span><span>${Math.floor(effectiveAttendance)}/${MAX_ATTENDANCE} (${attendancePercentage.toFixed(2)}%) [includes excused]</span></div>`;
    
    html += `<div class="stat-row"><span>Lab Work 1:</span><span>${lab1.toFixed(2)}</span></div>
        <div class="stat-row"><span>Lab Work 2:</span><span>${lab2.toFixed(2)}</span></div>
        <div class="stat-row"><span>Lab Work 3:</span><span>${lab3.toFixed(2)}</span></div>
        <div class="stat-row"><span>Lab Work Average:</span><span>${labWorkAverage.toFixed(2)}</span></div>
        <div class="stat-row"><span>Class Standing:</span><span>${classStanding.toFixed(2)}</span></div>
    </div>`;

    html += `<h3 style="margin-top: 20px; margin-bottom: 16px; color: var(--gray-900); font-weight: 700;">🎯 Required Prelim Exam Scores</h3>`;

    let passClass='warning', passIcon='⚠️', passRemark='';
    if(requiredPrelimForPassing>100){
        passClass='danger'; 
        passIcon='❌'; 
        passRemark='It is NOT possible to PASS. This exceeds the maximum possible score (100). You have failed even if you score 100 on the Prelim Exam.';
    }
    else if(requiredPrelimForPassing<=0){
        passClass='success'; 
        passIcon='🎉'; 
        passRemark='You have already secured a passing grade! You will pass even with a score of 0.';
    }
    else{
        passClass='success';
        passIcon='💪';
        passRemark='You need to score at least this on the Prelim Exam to pass.';
    }

    html += `<div class="requirement-box ${passClass}">
        <div class="requirement-title">${passIcon} To PASS (75%)</div>
        <div class="requirement-score">${requiredPrelimForPassing.toFixed(2)}</div>
        <div class="requirement-remark">${passRemark}</div>
    </div>`;

    let exClass='warning', exIcon='⚠️', exRemark='';
    if(requiredPrelimForExcellent>100){
        exClass='danger'; 
        exIcon='❌'; 
        exRemark='It is impossible to achieve an excellent grade of 100. The required score exceeds 100.';
    }
    else if(requiredPrelimForExcellent<=0){
        exClass='success'; 
        exIcon='🌟'; 
        exRemark='You have already achieved excellent standing! You will get 100 with any Prelim Exam score.';
    }
    else if(requiredPrelimForExcellent<=50){
        exClass='success'; 
        exIcon='🎯'; 
        exRemark='Great job! Your class standing is excellent. You only need a moderate score on the Prelim Exam to achieve a perfect grade.';
    }
    else{
        exClass='warning';
        exIcon='🎓';
        exRemark='You need to score at least this on the Prelim Exam to achieve an excellent grade of 100.';
    }

    html += `<div class="requirement-box ${exClass}">
        <div class="requirement-title">${exIcon} To achieve EXCELLENT (100%)</div>
        <div class="requirement-score">${requiredPrelimForExcellent.toFixed(2)}</div>
        <div class="requirement-remark">${exRemark}</div>
    </div>`;

    resultsContent.innerHTML = html;
    resultsCard.classList.add('show');
    setTimeout(()=>resultsCard.scrollIntoView({behavior:'smooth', block:'nearest'}),100);
}

function clearForm(){
    document.getElementById('attendance').value='';
    document.getElementById('lab1').value='';
    document.getElementById('lab2').value='';
    document.getElementById('lab3').value='';
    document.getElementById('resultsCard').classList.remove('show');
    document.getElementById('attendance').focus();
}

window.addEventListener('load', ()=>document.getElementById('attendance').focus());