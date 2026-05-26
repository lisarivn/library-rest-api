// Global variables
let currentLibId = null;
let currentDepId = null;
let currentSubId = null;
let editingLibraryId = null;
let editingDepId = null;
let editingSubId = null;
let editingLitId = null;

// Initialization
document.addEventListener("DOMContentLoaded", () => {
    handleRouting();
    checkAuthState();
});

window.addEventListener('popstate', () => handleRouting());

function handleRouting() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const params = new URLSearchParams(window.location.search);
    const path = window.location.pathname;

    if (path === '/' || path === '' || path.endsWith('/index.html')) {
        showLibrariesView();
    } else if (path === '/departments' && params.has('lib')) {
        currentLibId = params.get('lib');
        showDepartmentsView(currentLibId, 'Departments');
    } else if (path === '/subjects' && params.has('dep')) {
        currentDepId = params.get('dep');
        showSubjectsView(currentDepId, 'Subjects');
    } else if (path === '/literature' && params.has('sub')) {
        currentSubId = params.get('sub');
        showLiteratureView(currentSubId, 'Literature');
    }
}

// Auth
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) { return null; }
}

function checkAuthState() {
    const token = localStorage.getItem('token');
    const isLoggedIn = !!token;
    
    document.getElementById('login-form').style.display = isLoggedIn ? 'none' : 'block';
    document.getElementById('logout-btn').style.display = isLoggedIn ? 'block' : 'none';
    
    if (isLoggedIn) {
        handleRouting();
    } else {
        showView(null);
    }
}

async function login(username, password) {
    try {
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            window.history.pushState(null, '', '/');
            checkAuthState(); 
        } else alert("Incorrect username or password!");
    } catch (e) { console.error("Network error:", e); }
}

function logout() {
    localStorage.removeItem('token');
    window.history.pushState(null, '', '/');
    checkAuthState();
}

// Navigation and view control
function showView(viewId) {
    document.getElementById('libraries-view').style.display = (viewId === 'lib') ? 'block' : 'none';
    document.getElementById('departments-view').style.display = (viewId === 'dep') ? 'block' : 'none';
    document.getElementById('subjects-view').style.display = (viewId === 'sub') ? 'block' : 'none';
    const litView = document.getElementById('literature-view');
    if(litView) litView.style.display = (viewId === 'lit') ? 'block' : 'none';
}

function showLibrariesView() {
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
    document.getElementById('lib-admin-panel').style.display = isAdmin ? 'block' : 'none';

    window.history.pushState(null, '', '/');
    showView('lib');
    fetchLibraries();
}

function showDepartmentsView(libId, libName) {
    currentLibId = libId;
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
    document.getElementById('dep-admin-panel').style.display = isAdmin ? 'block' : 'none';

    document.getElementById('dep-page-title').innerText = `Departments: ${libName}`;
    window.history.pushState(null, '', `/departments?lib=${libId}`);
    showView('dep');
    fetchDepartments();
}

function showSubjectsView(depId, depName) {
    currentDepId = depId;
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
    document.getElementById('sub-admin-panel').style.display = isAdmin ? 'block' : 'none';

    document.getElementById('sub-page-title').innerText = `Subjects: ${depName}`;
    window.history.pushState(null, '', `/subjects?dep=${depId}`);
    showView('sub');
    fetchSubjects();
}

function showLiteratureView(subId, subName) {
    currentSubId = subId;
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
    document.getElementById('lit-admin-panel').style.display = isAdmin ? 'block' : 'none';

    document.getElementById('lit-page-title').innerText = `Literature: ${subName}`;
    window.history.pushState(null, '', `/literature?sub=${subId}`);
    showView('lit');
    fetchLiterature(subId);
}


// API CRUD
async function fetchLibraries() {
    try {
        const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
        const res = await fetch('/api/v1/libraries', { headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
        const data = await res.json();
        const container = document.getElementById('libraries-list');
        
        container.innerHTML = '<ul>' + data.map(l => `
            <li>
                <div><strong>${l.name}</strong><br><small>Departments: ${l.totalDepartments || l.total_departments || 0}</small></div>
                <div>
                    <button class="dep-btn" onclick="showDepartmentsView('${l.id}', '${l.name}')">Departments ➔</button>
                    ${isAdmin ? `
                        <button class="edit-btn" onclick="startLibEdit('${l.id}', '${l.name}', ${l.totalDepartments || l.total_departments || 0})">✎</button>
                        <button class="delete-btn" onclick="deleteLibrary('${l.id}')">✖</button>
                    ` : ''}
                </div>
            </li>`).join('') + '</ul>';
    } catch (err) {
        console.error("Error loading libraries:", err);
    }
}

async function fetchDepartments() {
    try {
        const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
        const res = await fetch(`/api/v1/libraries/${currentLibId}/departments`, { 
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } 
        });
        const data = await res.json();
        const container = document.getElementById('departments-list');
        
        container.innerHTML = '<ul>' + data.map(d => `
            <li>
                <div>
                    <strong>${d.name}${d.spec ? ' <span style="font-size: 0.75em; background-color: #fff0f1; color: var(--danger); padding: 3px 8px; border-radius: 4px; margin-left: 8px; font-weight: 600; border: 1px solid #fecdd3;">🔒 Specialized</span>' : ''}</strong>
                    <br><small>Dean: ${d.dean}</small>
                </div>
                <div class="action-btns">
                    <button class="action-btn" onclick="showSubjectsView('${d.id}', '${d.name}')">Subjects ➔</button>
                    ${isAdmin ? `
                        <button class="edit-btn" onclick="startDepEdit('${d.id}', '${d.name}', '${d.dean}', ${d.spec})">✎</button>
                        <button class="delete-btn" onclick="deleteDepartment('${d.id}')">✖</button>
                    ` : ''}
                </div>
            </li>`).join('') + '</ul>';
    } catch (err) {
        console.error("Error fetching departments:", err);
    }
}

async function fetchSubjects() {
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';
    
    const res = await fetch(`/api/v1/subjects/by-department/${currentDepId}`, { headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    const data = await res.json();
    const container = document.getElementById('subjects-list');
    
    container.innerHTML = data.map(s => `
        <div class="sub-item">
            <strong>${s.name}</strong> 
            <span>${s.credits} cr.</span> 
            <span>${s.syllabus}</span>
            <div class="action-btns">
                <button class="dep-btn" onclick="showLiteratureView('${s.id}', '${s.name}')">📚 Literature ➔</button>
                ${isAdmin ? `
                    <button class="edit-btn" onclick="startSubEdit('${s.id}','${s.name}',${s.credits},'${s.syllabus}')">✎</button>
                    <button class="delete-btn" onclick="deleteSubject('${s.id}')">✖</button>
                ` : ''}
            </div>
        </div>`).join('');
}

async function fetchLiterature(subId) {
    const isAdmin = parseJwt(localStorage.getItem('token'))?.role === 'ROLE_ADMIN';

    const res = await fetch(`/api/v1/literature/by-subject/${subId}`, { headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    const data = await res.json();
    
    document.getElementById('literature-list').innerHTML = data.map(l => `
        <div class="sub-item">
            <strong>${l.name}</strong> 
            <span>${l.publicationYear} year.</span> 
            <span>${l.author}</span>
            <div class="action-btns">
                ${isAdmin ? `
                    <button class="edit-btn" onclick="startLitEdit('${l.id}','${l.name}','${l.author}',${l.publicationYear})">✎</button>
                    <button class="delete-btn" onclick="deleteLiterature('${l.id}')">✖</button>
                ` : ''}
            </div>
        </div>
    `).join('');
}

// delete functions
async function deleteLibrary(id) {
    if (!confirm("Delete the library?")) return;
    await fetch(`/api/v1/libraries/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    fetchLibraries();
}

async function deleteDepartment(id) {
    if (!confirm("Delete the department?")) return;
    await fetch(`/api/v1/departments/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    fetchDepartments();
}

async function deleteSubject(id) {
    if (!confirm("Delete the subject?")) return;
    await fetch(`/api/v1/subjects/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    fetchSubjects();
}

async function deleteLiterature(id) {
    if (!confirm("Delete the book?")) return;
    await fetch(`/api/v1/literature/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } });
    fetchLiterature(currentSubId);
}

// save functions
async function saveSubject() {
    const body = { 
        name: document.getElementById('sub-name').value,
        credits: parseInt(document.getElementById('sub-credits').value),
        syllabus: document.getElementById('sub-syllabus').value,
        department: { id: currentDepId }
    };
    
    const isEdit = editingSubId !== null;
    const url = isEdit ? `/api/v1/subjects/${editingSubId}` : '/api/v1/subjects';
    
    const response = await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    if (response.ok) { cancelSubEdit(); fetchSubjects(); }
}

async function saveLiterature() {
    const body = { 
        name: document.getElementById('lit-name').value,
        author: document.getElementById('lit-author').value,
        publicationYear: parseInt(document.getElementById('lit-year').value),
        subject: { id: currentSubId } 
    };
    
    const isEdit = editingLitId !== null;
    const url = isEdit ? `/api/v1/literature/${editingLitId}` : '/api/v1/literature';
    
    const response = await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    if (response.ok) { cancelLitEdit(); fetchLiterature(currentSubId); }
}

// start/cancelEdit functions
function startSubEdit(id, name, credits, syllabus) {
    editingSubId = id;
    document.getElementById('sub-name').value = name;
    document.getElementById('sub-credits').value = credits;
    document.getElementById('sub-syllabus').value = syllabus;
    document.getElementById('sub-panel-title').innerText = "📝 Editing a subject";
    document.getElementById('sub-submit-btn').innerText = "Save";
    document.getElementById('sub-cancel-btn').style.display = 'inline-block';
}

function cancelSubEdit() {
    editingSubId = null;
    document.getElementById('sub-name').value = '';
    document.getElementById('sub-credits').value = '';
    document.getElementById('sub-syllabus').value = '';
    document.getElementById('sub-panel-title').innerText = "🛠️ Add a subject";
    document.getElementById('sub-submit-btn').innerText = "Create";
    document.getElementById('sub-cancel-btn').style.display = 'none';
}

function startLitEdit(id, name, author, year) {
    editingLitId = id;
    document.getElementById('lit-name').value = name;
    document.getElementById('lit-author').value = author;
    document.getElementById('lit-year').value = year;
    document.getElementById('lit-panel-title').innerText = "📝 Editing a book";
    document.getElementById('lit-submit-btn').innerText = "Save";
    document.getElementById('lit-cancel-btn').style.display = 'inline-block';
}

function cancelLitEdit() {
    editingLitId = null;
    document.getElementById('lit-name').value = '';
    document.getElementById('lit-author').value = '';
    document.getElementById('lit-year').value = '';
    document.getElementById('lit-panel-title').innerText = "🛠️ Add a book";
    document.getElementById('lit-submit-btn').innerText = "Create";
    document.getElementById('lit-cancel-btn').style.display = 'none';
}

async function saveLibrary() {
    const name = document.getElementById('lib-name').value;
    const totalDepartments = parseInt(document.getElementById('lib-deps').value);
    if (!name) return alert("Enter a name!");

    const isEdit = editingLibraryId !== null;
    const url = isEdit ? `/api/v1/libraries/${editingLibraryId}` : '/api/v1/libraries';
    
    await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, totalDepartments })
    });
    
    cancelLibEdit();
    fetchLibraries();
}

function startLibEdit(id, name, deps) {
    editingLibraryId = id;
    document.getElementById('lib-name').value = name;
    document.getElementById('lib-deps').value = deps;
    document.getElementById('lib-panel-title').innerText = "📝 Editing a library";
    document.getElementById('submit-lib-btn').innerText = "Save"; 
    document.getElementById('cancel-lib-btn').style.display = 'inline-block';
}

function cancelLibEdit() {
    editingLibraryId = null;
    document.getElementById('lib-name').value = '';
    document.getElementById('lib-deps').value = '1';
    document.getElementById('lib-panel-title').innerText = "🛠️ Add a library";
    document.getElementById('submit-lib-btn').innerText = "Create"; 
    document.getElementById('cancel-lib-btn').style.display = 'none'; 
}

async function saveDepartment() {
    const name = document.getElementById('dep-name').value;
    const dean = document.getElementById('dep-dean').value;
    const spec = document.getElementById('dep-spec').checked;
    if (!name || !dean) return alert("Please fill in all the fields!");

    const isEdit = editingDepId !== null;
    const url = isEdit ? `/api/v1/departments/${editingDepId}` : '/api/v1/departments';
    
    await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, dean, spec, library: { id: currentLibId } })
    });

    cancelDepEdit();
    fetchDepartments();
}

function startDepEdit(id, name, dean, spec) {
    editingDepId = id;
    document.getElementById('dep-name').value = name;
    document.getElementById('dep-dean').value = dean;
    document.getElementById('dep-spec').checked = spec;
    document.getElementById('dep-panel-title').innerText = "📝 Editing a department";
    document.getElementById('submit-dep-btn').innerText = "Save";
    document.getElementById('cancel-dep-btn').style.display = 'inline-block';
}

function cancelDepEdit() {
    editingDepId = null;
    document.getElementById('dep-name').value = '';
    document.getElementById('dep-dean').value = '';
    document.getElementById('dep-spec').checked = false;
    document.getElementById('dep-panel-title').innerText = "🛠️ Add a department";
    document.getElementById('submit-dep-btn').innerText = "Save";
    document.getElementById('cancel-dep-btn').style.display = 'none';
}