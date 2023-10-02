import React, { useState } from 'react';
import styled from 'styled-components';
import useIcon from '../../hooks/useIcon';
import * as S from '../../styles/search/SearchBar.styled';
import { palette } from '../../constants/Styles';

interface SearchBarProps {
  onSearch: (query: string) => void;
}

function SearchBar({ onSearch }: SearchBarProps) {
  const { IcSearch } = useIcon();
  const [text, setText] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(text);
  };

  return (
    <S.SearchForm onSubmit={handleSubmit}>
      <S.SearchInput
        type="text"
        placeholder="Search"
        value={text}
        onChange={(e) => setText(e.target.value)}
      />
      <S.SearchButton type="submit">
        <IcSearch size={3} color={palette.textPrimary} />
      </S.SearchButton>
    </S.SearchForm>
  );
}

export default SearchBar;
