describe('Logout spec', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept('GET', '/api/session', []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('should log out successfully and redirect to login page', () => {

    cy.visit('/session/')
    // Ensure the logout button or span is present and clickable
    cy.get('span').contains('Logout').should('be.visible').click()

    // Debug the redirection
    cy.url().should('include', '/login')
    cy.location('pathname').should('eq', '/login')  // Confirm the URL path

    // Optionally check that session information is cleared
    cy.window().its('localStorage').invoke('getItem', 'session').should('be.null')

    // Verify that the user sees the login form
    cy.get('input[formControlName=email]').should('exist')
    cy.get('input[formControlName=password]').should('exist')
  })
})
