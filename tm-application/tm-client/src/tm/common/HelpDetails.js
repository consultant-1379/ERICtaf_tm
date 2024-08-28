define(function () {
    'use strict';

    var HelpDetails = {};

    HelpDetails.data = {
        TEST_SPECIFICATION: 'A document that consists of a test design specification, ' +
                            'test case specification and/or test procedure specification.',
        TEST_CASE: 'A set of input values, execution preconditions, expected results and ' +
                   'execution postconditions, developed for a particular objective or test ' +
                   'condition, such as to exercise a particular program path or to verify ' +
                   'compliance with a specific requirement.',
        TEST_PLAN: 'A document describing the scope, approach, resources and schedule of intended ' +
                   'test activities. It identifies amongst others test items, the features to be tested, ' +
                   'the testing tasks, who will do each task.',
        TEST_CAMPAIGN_GROUPS: 'Test Campaign Group allows users to create and add their own test campaigns in to their own pre-defined group. ' +
                    'It will calculate all the test case executions for each test campaign and give an overall status to the user.',
        REQUIREMENT: 'A condition or capability needed by a user to solve a problem or achieve an ' +
                     'objective that must be met or possessed by a system or system component to satisfy ' +
                     'a contract, standard, specification, or other formally imposed document. In TMS a ' +
                     'requirement can be either an EPIC/User Story/MR or Improvement.'
    };

    return HelpDetails;
});
