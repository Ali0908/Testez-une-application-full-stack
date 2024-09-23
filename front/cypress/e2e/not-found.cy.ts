describe('Not Found Page', () => {
  it('should display a not found message', () => {
    // Visit a non-existent URL
    cy.visit('/non-existent-route');

    // Assertions to verify the not found page content
    // cy.title().should('include', 'Not Found');
    cy.get('h1').should('contain', 'Page not found !');
  });
});
