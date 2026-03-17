package com.unicauca.edu.co.auxiliary_book.domain.models.core.commands;

public interface IJobCommand {
    void execute(JobCommandContext context);
}
