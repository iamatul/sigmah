/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.bootstrap.model;

import org.sigmah.server.domain.User;

public class ConfirmInvitePageModel extends PageModel {
    private User user;
    private boolean formIncomplete;

    public ConfirmInvitePageModel(User user) {
        this.setUser(user);
    }

    public static ConfirmInvitePageModel incompleteForm(User user) {
        ConfirmInvitePageModel model = new ConfirmInvitePageModel(user);
        model.setFormIncomplete(true);
        return model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFormIncomplete() {
        return formIncomplete;
    }

    public void setFormIncomplete(boolean formIncomplete) {
        this.formIncomplete = formIncomplete;
    }
}